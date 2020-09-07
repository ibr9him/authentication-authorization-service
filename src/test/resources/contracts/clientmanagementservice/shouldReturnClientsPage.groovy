import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return a filtered page of client"
    request {
        headers {
            contentType(applicationJson())
        }
        method GET()
        url("/api/v1/clients?page=0&size=10&sort=id,decs&q=")
    }
    response {
        status 200
        body('''
            {
                "content": [
                    {
                        "id": "CLIENT_ID",
                        "name": "NAME",
                        "nameAr": "NAME_AR"
                    }
                ],
                "pageable": {
                    "sort": {
                        "sorted": "true",
                        "unsorted": "false",
                        "empty": "false"
                    },
                    "pageNumber": 0,
                    "pageSize": 10,
                    "offset": 0,
                    "paged": true,
                    "unpaged": false
                },
                "totalElements": 1,
                "totalPages": 1,
                "last": true,
                "numberOfElements": 1,
                "first": true,
                "size": 10,
                "number": 0,
                "sort": {
                    "sorted": true,
                    "unsorted": false,
                    "empty": false
                },
                "empty": false
            }
        ''')
        headers {
            contentType(applicationJson())
        }
    }
}
