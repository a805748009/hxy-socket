package nafos.protocol

import nafos.server.handle.SocketPieplineDynamicHandle
import nafos.server.start.NafosRunner
import org.springframework.stereotype.Component

@Component
class Configuration : NafosRunner{
    override fun run() {
        SocketPieplineDynamicHandle.clazz = PorotcolSocketPieplineDynamicHandle::class.java
    }
}