import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should create a new client"
    request {
        body('''
                { 
                    "id" : "",
                    "name": "NAME",
                    "nameAr": "NAME_AR" 
                }
        ''')
        headers {
            contentType(applicationJson())
        }
        method POST()
        url("/api/v1/clients/")
    }
    response {
        status 201
        body('''
                { 
                    "id" : "CLIENT_ID",
                    "name": "NAME",
                    "nameAr": "NAME_AR" 
                }
        ''')
        headers {
            contentType(applicationJson())
        }
    }
}
