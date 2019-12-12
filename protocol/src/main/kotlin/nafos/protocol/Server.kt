package nafos.protocol

import nafos.server.NafosServer


fun protocolSocketServer(): NafosServer {
    NafosServer.configuration = ProtocolSocketConfiguration()
    return NafosServer()
}

fun socketServer(configuration: ProtocolSocketConfiguration): NafosServer {
    NafosServer.configuration = configuration
    return NafosServer()
}
