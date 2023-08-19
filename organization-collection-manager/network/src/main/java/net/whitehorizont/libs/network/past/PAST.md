# Arbitrary sized package transfer - PAST protocol

 > *Note*: Max UDP packet size is 64 KiB. UDP breaks packets which exceed MTU into fragments. At least one fragment lost = packet  lost

## Definitions

Packet - unit of data transfer with transport dependent maximum
 length (such as UDP or USB packet. Does not take TCP into account
 because it is stream based protocol. Although,
  TCP has MTU, it is already abstracted away 
  by the steam nature of protocol).

Package - unit of payload of this protocol. 
May be *packaged* into multiple *packets* if exceeds
 maximum length of packet.

Payload - arbitrary binary data. Headers are never part of payload

Maximum Package length is $\approx2^{64}$ bytes.

## Goals

 - Create transport agnostic protocol
 - Protocol should sent arbitrary sized units of binary data. That is protocol is *package* oriented rather then *stream* oriented. Though each individual package (especially large one) can be seen as stream with known length.


### Rationale

Allowing to send arbitrary binary data in arbitrary length packages allows to completely abstract away underlying transport (whether it be TCP or UDP).

Considering each package as separate stream allows logic segregation of binary data without need to parse it.

*Side note*: Since protocol is package oriented, it may specify maximum package length. Making maximum length optional allows to use stream based transports with guaranteed delivery (such as TCP) without overhead. Maximum packages length is required to determine whether to package data into `SIMPLE_PACKAGE` or into `LONG_PACKAGE`. Opt-outing it would allow to send large data chunks within single SIMPLE_PACKAGE.

##  layout

 > Types are denoted as either `u` (means unsigned int) or `i` (means signed int) combined with number (designates number of bits, including sing). E.g. `i16` means: 16 bits signed integer

 These fields are ***mandatory for all packets***:

| name        | type  | description                                                                                                                                |
| ----------- | ----- | ------------------------------------------------------------------------------------------------------------------------------------------ |
| version     | `u16` | Protocol version. Required for backward compatibility. Value `0` is special and reserved. It should be used **only for testing purposes!** |
| packet_type | `u8`  | [Packet Type](#packet-type)                                                                                                                |

On breaking change, version value is incremented starting from `1`.

`1` is version of initial release of protocol

### Packet type
Each packet type may specify its own additional fields.

**packet_type** is enum:
| enum constant  | value | description                                                                                                                                                                            |
| -------------- | :---: | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| CONTROL_PACKET |   0   |                                                                                                                                                                                        |
| SIMPLE_PACKAGE |   1   |                                                                                                                                                                                        |
| LONG_PACKAGE   |   2   | (LONG_PACKAGE_* types are only valid when maximum package length is provided. Nevertheless server should gracefully respond to LONG_PACKAGE_* even if transport does not require them) |

Length always represents length in *bytes*

#### SIMPLE_PACKAGE

| name   | type  | description       |
| ------ | ----- | ----------------- |
| length | `u16` | length of payload |

`SIMPLE_PACKAGE` transfers entire payload within single `packet`. If maximum packet length is provided and total package length exceeds provided packet length limit, then package should be split into multiple packets of type `LONG_PACKAGE`. If transport layer does not have length limit and huge `SIMPLE_PACKAGE` is sent, it blocks transport until whole payload is transferred. Therefore ***it is not recommended to set transport limit too high!***

#### LONG_PACKAGE 

 | name         | type        | description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
 | ------------ | ----------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
 | transfer_id  | `u64`       | unique identifier generated randomly. Sent packets and received packets has distinct `transfer_id` namespaces. That means, same `transfer_id` can be present in both sent and received packets. `transfer_id` must be unique within single namespace.<br> Request id is remembered by both sides until a whole package is received. This means that no other request can be sent with this id, until request is complete. When request is complete it's ID is freed                                                                                                           |
 | total_length | `u64`       | total number of bytes of payload to deliver (i.e. package length). Headers are never included in total length. Required to allocate enough space for entire payload. Should be the same for all packets with same `transfer_id` (because we don't know which packet would arrive first). When first packet of transfer received, its `total_length` value used. The field will be ignored for all other packets of transfer. Receiving more bytes then `total_length` is considered an error: excess bytes are discarded, `CONTROL_PACKET` of `ERROR` type is sent to client. |
 | offset       | `u64`       | offset into payload buffer                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
 | length       | `u16`       | number of bytes of `payload` (i.e. length of chunk of package) contained within this packet                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
 | payload      | binary data |
 
Long packages allows all types of packages to interleave

 > *Note*: we could make distinct packet type 
 > to init LONG_PACKAGE transfer, but that
 > would *complicate* protocol, while providing *small*
 > advantage in total throughput

 > *Note*: we could make packet number or something in place of `offset`. 
 > But that would complicated receiving out of order packets. When
 > packet arrives out of order we would not be able to immediately place
 > it into buffer, because we wouldn't know sizes of undelivered 
 > packets yet. Therefore wouldn't be able to calculate the exact offset
 > into buffer.

Package is considered received when number of payload bytes received is equal to `total_length`.

#### CONTROL_PACKAGE

Additional mandatory fields:

| name         | type  | description                       |
| ------------ | ----- | --------------------------------- |
| control_type | `u16` | Specifies type of control request |

 **control_type** is enum:

| enum constant | value | description                              |
| ------------- | ----- | ---------------------------------------- |
| ERROR         |       | Delivers info about error on either side |


> *Note*: Control packets may specify type dependent payload. 

`transfer_id` (and in case of LONG_PACKAGE packet offset) allows out of order packages and multiple simultaneous transfers within same socket connection.
