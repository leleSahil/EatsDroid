package com.company.client;

import com.company.sockets.AuthenticationChecker;
import com.company.sockets.Request;
import com.company.sockets.Response;

/**
 * Created by brian on 4/16/16.
 */
public class ClientExample {

    public static void main(String[] args){
        // Correct auth to be included on requests
        Request.RequestAuthentication auth = new Request.RequestAuthentication("admin", AuthenticationChecker.hash("password"));


        // Login request
        Request request = new Request(null, new Request.RequestCheckAuthentication("admin", AuthenticationChecker.hash("password")));
        new Client(request, new ResponseInterface() {

            Response resp;
            @Override
            public void callback(Response resp) {
               this.resp = resp;
            }

            @Override
            public void run() {
                System.out.println("SOCKETS: Callback called");
                if(this.resp != null && this.resp.requestSuccess){
                    System.out.println("Success");
                }else{
                    System.out.println("Failure");
                }

            }
        }).send();


        // Menu Add Request


        // Menu Delete Request


        // Menu Pull Request




        // Pragma: Request
//        public static class RequestMenuAdd extends  RequestMenuUpdate implements Serializable {
//            String food_name;
//            public RequestMenuAdd(String _id, String meal_identifier, String section_identifier, String food_identifier, String food_name) {
//                super(_id, meal_identifier, section_identifier, food_identifier);
//                this.food_name = food_name;
//            }
//        }
//
//        // Pragma: Request
//        public static class RequestMenuDelete extends  RequestMenuUpdate implements Serializable{
//            public RequestMenuDelete(String _id, String meal_identifier, String section_identifier, String food_identifier) {
//                super(_id, meal_identifier, section_identifier, food_identifier);
//            }
//        }
//
//        // Pragma: Request
//        public static class RequestPullMenus implements Serializable{
//            Calendar date;
//        }







    }

}
