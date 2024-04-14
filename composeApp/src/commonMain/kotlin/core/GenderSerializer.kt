package core

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object GenderSerializer : KSerializer<Gender> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Gender", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Gender) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): Gender {
        return try {
            when(decoder.decodeString().lowercase()) {
                "male" -> Gender.MALE
                "female" -> Gender.FEMALE
                "unknown" -> Gender.UNKNOWN
                "n/a" -> Gender.NOT_AVAILABLE
                "hermaphrodite" -> Gender.HERMAPHRODITE
                else -> throw RuntimeException("")
            }
        } catch (e: Exception) {
            Gender.NOT_AVAILABLE
        }
    }
}
