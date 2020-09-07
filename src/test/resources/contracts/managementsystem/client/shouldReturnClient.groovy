//package contracts.managementsystem.client
//
//import org.springframework.cloud.contract.spec.Contract
//
//Contract.make {
//    description "should return client by id=CLIENT_ID"
//
//    request {
//        url "/api/v1/clients/CLIENT_ID"
//        method GET()
//    }
//
//    response {
//        status OK()
//        headers {
//            contentType applicationJson()
//        }
//        body (
//                id: "CLIENT_ID",
//                name: "NAME",
//                nameAr: "NAME_AR"
//        )
//    }
//}
