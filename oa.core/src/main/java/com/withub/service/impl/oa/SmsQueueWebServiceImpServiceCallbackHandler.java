
/**
 * SmsQueueWebServiceImpServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.withub.service.impl.oa;

    /**
     *  SmsQueueWebServiceImpServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class SmsQueueWebServiceImpServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public SmsQueueWebServiceImpServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public SmsQueueWebServiceImpServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for messageToQueue method
            * override this method for handling normal response from messageToQueue operation
            */
           public void receiveResultmessageToQueue(
                    com.withub.service.impl.oa.SmsQueueWebServiceImpServiceStub.MessageToQueueResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from messageToQueue operation
           */
            public void receiveErrormessageToQueue(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryByPhone method
            * override this method for handling normal response from queryByPhone operation
            */
           public void receiveResultqueryByPhone(
                    com.withub.service.impl.oa.SmsQueueWebServiceImpServiceStub.QueryByPhoneResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryByPhone operation
           */
            public void receiveErrorqueryByPhone(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryByFlow method
            * override this method for handling normal response from queryByFlow operation
            */
           public void receiveResultqueryByFlow(
                    com.withub.service.impl.oa.SmsQueueWebServiceImpServiceStub.QueryByFlowResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryByFlow operation
           */
            public void receiveErrorqueryByFlow(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteFlow method
            * override this method for handling normal response from deleteFlow operation
            */
           public void receiveResultdeleteFlow(
                    com.withub.service.impl.oa.SmsQueueWebServiceImpServiceStub.DeleteFlowResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteFlow operation
           */
            public void receiveErrordeleteFlow(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryRecContentByFlow method
            * override this method for handling normal response from queryRecContentByFlow operation
            */
           public void receiveResultqueryRecContentByFlow(
                    com.withub.service.impl.oa.SmsQueueWebServiceImpServiceStub.QueryRecContentByFlowResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryRecContentByFlow operation
           */
            public void receiveErrorqueryRecContentByFlow(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryRecContentByBusiCode method
            * override this method for handling normal response from queryRecContentByBusiCode operation
            */
           public void receiveResultqueryRecContentByBusiCode(
                    com.withub.service.impl.oa.SmsQueueWebServiceImpServiceStub.QueryRecContentByBusiCodeResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryRecContentByBusiCode operation
           */
            public void receiveErrorqueryRecContentByBusiCode(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryRecContent method
            * override this method for handling normal response from queryRecContent operation
            */
           public void receiveResultqueryRecContent(
                    com.withub.service.impl.oa.SmsQueueWebServiceImpServiceStub.QueryRecContentResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryRecContent operation
           */
            public void receiveErrorqueryRecContent(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteRecContent method
            * override this method for handling normal response from deleteRecContent operation
            */
           public void receiveResultdeleteRecContent(
                    com.withub.service.impl.oa.SmsQueueWebServiceImpServiceStub.DeleteRecContentResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteRecContent operation
           */
            public void receiveErrordeleteRecContent(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for previewMessage method
            * override this method for handling normal response from previewMessage operation
            */
           public void receiveResultpreviewMessage(
                    com.withub.service.impl.oa.SmsQueueWebServiceImpServiceStub.PreviewMessageResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from previewMessage operation
           */
            public void receiveErrorpreviewMessage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createSmsQueue method
            * override this method for handling normal response from createSmsQueue operation
            */
           public void receiveResultcreateSmsQueue(
                    com.withub.service.impl.oa.SmsQueueWebServiceImpServiceStub.CreateSmsQueueResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createSmsQueue operation
           */
            public void receiveErrorcreateSmsQueue(java.lang.Exception e) {
            }
                


    }
    