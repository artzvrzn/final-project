<h3>FINANCE MANAGEMENT API</h3>
<hr>
<p><b>REST</b>ful application, that provides an API for finance management. Built using <b>Java</b> and <b>Spring Framework</b> and based on <b>microservices</b> architecture, which allows to easily maintain and scale the app. Consists of separate modules, each responsible for its own functionality:
<ul>
<li> accounts and financial operations management;</li>
<li> control over app currencies and categories;</li>
<li> generation of xlsx format reports with <b>Apache POI</b>;</li>
<li> sending a report by email;</li>
<li> planning of operations or emails, according to a schedule via <b>Quartz</b>;</li>
<li> integration with Telegram as a UI;</li>
<li> supports multi-user usage via <b>JWT</b>. </li>
</ul>
</p>
<p>The application has been packaged with Docker and has been deployed to the remote server.</p>
<hr>
<p><b>Tech stack</b>: Java 11, Spring Framework (Boot, Data JPA, MVC, Data JPA), PostgreSQL, Quartz, Apache POI, MinIO, Docker, Swagger.</p>
