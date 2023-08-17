
# Network stack architecture

Our network stack consists of these layers:
 - Serialization/deserialization layer
 - Logical layer
 - Transport layer

## Serialize/deserialize layer

The layer takes arbitrary object (`Serializable`) and serializes it into sequence of bytes (byte array or byte buffer) using java standard serialization mechanism. Then, obtained bytes are passed to [logical layer](#logic-layer)

## Logic layer

Packages arbitrary byte array or byte buffer into sequence of packets of size no more than provided maximum. Maximum length of packet is deduced from restrictions of transport layer. This is up to logic layer to define how initial byte array corresponds to packets. Packets then are passed to [transport layer](#transport-layer)


# Transport layer

Receives byte array or byte buffer with length, lower then maximum allowed. The layer must discard data that exceeds length limit. The layer sends data through actual transport such as udp or tcp or usb.

