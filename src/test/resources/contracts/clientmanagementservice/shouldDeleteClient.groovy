import org.springframework.cloud.contract.spec.Contract

Contract.make {
    //$(regex('[0-9]{10}')) use this for the instead of CLIENT_ID
    description "should delete a client given it id=CLIENT_ID"
    request {
        headers {
            contentType(applicationJson())
        }
        method DELETE()
        url("/api/v1/clients/CLIENT_ID")
    }
    response {
        status 200
    }
}
