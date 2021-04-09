package br.com.zup

import io.grpc.Status
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import kotlin.random.Random

//A annotation serve para que o Micronaut reconheça a classe
@Singleton
class FreteGrpcServer : FreteServiceGrpc.FreteServiceImplBase() {

    private val logger = LoggerFactory.getLogger(FreteGrpcServer::class.java)
    override fun calculaFrete(request: CalculaFreteRequest?, responseObserver: StreamObserver<CalculaFreteResponse>?) {
        logger.info("Calculando frete para request: $request")

        val cep = request!!.cep

        if (cep == null || cep.isBlank()) {
            val erro = Status.INVALID_ARGUMENT.withDescription("Cep deve ser informado").asRuntimeException()

            responseObserver?.onError(erro)
        }

        val response = CalculaFreteResponse.newBuilder()
            .setCep(request!!.cep)
            .setValor(Random.nextDouble(from = 0.0, until = 140.0))
            .build()

        logger.info("Frete calculado: $response")
        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}