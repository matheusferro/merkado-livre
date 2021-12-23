package com.merkadoLivreGrpc.testeSNSeSQS

import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import java.net.URI
import javax.inject.Singleton

@Singleton
class SqsTest {

    fun producer() {
        val sqsClient: SqsClient = SqsClient.builder().region(Region.SA_EAST_1).endpointOverride(URI.create("http://localhost:4566")).build()
        val messageAttribute: Map<String, MessageAttributeValue> = mapOf(
            "Name" to MessageAttributeValue.builder().stringValue("Matheus").build()
        )
        val sendMessageRequest = SendMessageRequest.builder()
            //.messageAttributes(messageAttribute) 
            .messageBody("aqui é o body!!")
            .queueUrl("http://localhost:4566/000000000000/queueBraba").build()
        sqsClient.sendMessage(sendMessageRequest)
    }

    fun consumer(){
        val sqsClient: SqsClient = SqsClient.builder().region(Region.SA_EAST_1).endpointOverride(URI.create("http://localhost:4566")).build()
        val receiveMessageRequest = ReceiveMessageRequest.builder()
            .queueUrl("http://localhost:4566/000000000000/queueBraba")
            .maxNumberOfMessages(10)
            .waitTimeSeconds(2)
            .build()
        sqsClient.receiveMessage(receiveMessageRequest).messages().forEach {
            println(it.body())
        }
    }
}