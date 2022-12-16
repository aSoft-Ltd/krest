package epsilon

import koncurrent.Executor
import koncurrent.Later

class ByteArrayBlob(val value: ByteArray) : Blob {
    override fun readBytes(executor: Executor): Later<ByteArray> = Later.resolve(value, executor)
}