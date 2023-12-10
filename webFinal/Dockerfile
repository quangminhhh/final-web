FROM tomcat:9.0.79-jdk17-corretto-al2
RUN rm -rf /usr/local/tomcat/webapps/ROOT
RUN rm -rf /usr/local/tomcat/webapps/examples
ADD webFinal.war /usr/local/tomcat/webapps/

EXPOSE 8080
CMD ["catalina.sh", "run"]