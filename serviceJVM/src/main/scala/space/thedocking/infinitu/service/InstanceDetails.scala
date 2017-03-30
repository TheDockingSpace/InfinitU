package space.thedocking.infinitu.service

import org.apache.curator.x.discovery.UriSpec
import org.codehaus.jackson.map.annotate.JsonRootName

//curator uses jackson serializers
@JsonRootName("details")
case class InstanceDetails(
    serviceName: String = "service", 
    serviceHost: String = "localhost", 
    servicePort: Int = 9876, 
    servicePath: String = "/service", 
    serviceDescription: String = "description") {
  
    val uriSpec= new UriSpec(s"{scheme}://$serviceHost:{port}")

    //jackson requires this constructor
    def this() = {
        this("service")
    }
    
}
