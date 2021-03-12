## Spring Boot Cloud Example Project

This is a microservices project using spring boot-cloud dependencies, this project was implemented with service-discovery, api gateway, and Queue for async communication. Each microservice have a isolated database
<hr/>
<img src="contributed/diagram.png" />

<ul>
    <li>
        <b>Api Gateway (gateway-service)</b>: are implemented with the <b>spring-cloud-starter-netflix-zuul</b> dependency.
    </li>
    <li>
        <b>Discovery Service (discovery-service)</b>: are implemented with the 
        <b> spring-cloud-starter-netflix-eureka-server</b> dependency.
    </li>
    <li>
        <b>Auth Service (auth-service)</b>: Service that work with user creation and generate user authorization token (JWT Token).
    </li>
    <li>
        <b>Product Service (product-service)</b>: Service that work with product managment and it have a RabbitMQ Producer for notify the payment-service about products chanfes.
    </li>
    <li>
        <b>Payment Service (payment-service)</b>: Service that work with sale registration and it have a RabbitMQ Consumer for receive notifications about products changes, and use it to update product inventory in self database.
    </li>
    <li>
        <b>linecodeframework</b>: It's a dependency with commun resources in all projects like JWT token validation, web security configuration, DTO and Entity mapper interfaces, validations utility, RestException's, Exception handler configurationconfiguration.
    </li>
</ul>