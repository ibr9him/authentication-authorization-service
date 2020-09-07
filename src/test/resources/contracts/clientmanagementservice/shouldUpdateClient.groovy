import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should update an existing client"
    request {
        body('''
                { 
                    "id" : "CLIENT_ID",
                    "name": "name",
                    "nameAr": "name_ar" 
                }
        ''')
        headers {
            contentType(applicationJson())
        }
        method PUT()
        url("/api/v1/clients/")
    }
    response {
        status 200
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
