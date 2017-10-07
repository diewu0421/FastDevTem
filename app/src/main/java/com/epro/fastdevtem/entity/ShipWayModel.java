package com.epro.fastdevtem.entity;

import java.util.List;

/**
 * ShipWayModel
 *
 * @author zlw
 * @date 2017/9/28
 */

public class ShipWayModel{

    /**
     * Status : 0
     * Data : {"Error":null,"Model":[{"DeliveryTime":"7-10","Cost":5,"IsTracking":false,"ShippingMethod":"SuperSaver"},{"DeliveryTime":"7-10","Cost":6.7,"IsTracking":true,"ShippingMethod":"Standard"},{"DeliveryTime":"4-7","Cost":5,"IsTracking":true,"ShippingMethod":"Expedited"}],"CurrencyCode":"USD","CurrencyCodeSymbol":"US$"}
     * RowCount : 3
     */

    private String Status;
    private DataBean Data;
    private int RowCount;

    @Override
    public String toString() {
        return "ShipWayModel{" +
                "Status='" + Status + '\'' +
                ", Data=" + Data +
                ", RowCount=" + RowCount +
                '}';
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public int getRowCount() {
        return RowCount;
    }

    public void setRowCount(int RowCount) {
        this.RowCount = RowCount;
    }

    public static class DataBean {
        /**
         * Error : null
         * Model : [{"DeliveryTime":"7-10","Cost":5,"IsTracking":false,"ShippingMethod":"SuperSaver"},{"DeliveryTime":"7-10","Cost":6.7,"IsTracking":true,"ShippingMethod":"Standard"},{"DeliveryTime":"4-7","Cost":5,"IsTracking":true,"ShippingMethod":"Expedited"}]
         * CurrencyCode : USD
         * CurrencyCodeSymbol : US$
         */

        private Object Error;

        @Override
        public String toString() {
            return "DataBean{" +
                    "Error=" + Error +
                    ", CurrencyCode='" + CurrencyCode + '\'' +
                    ", CurrencyCodeSymbol='" + CurrencyCodeSymbol + '\'' +
                    ", Model=" + Model +
                    '}';
        }

        private String CurrencyCode;
        private String CurrencyCodeSymbol;
        private List<ModelBean> Model;

        public Object getError() {
            return Error;
        }

        public void setError(Object Error) {
            this.Error = Error;
        }

        public String getCurrencyCode() {
            return CurrencyCode;
        }

        public void setCurrencyCode(String CurrencyCode) {
            this.CurrencyCode = CurrencyCode;
        }

        public String getCurrencyCodeSymbol() {
            return CurrencyCodeSymbol;
        }

        public void setCurrencyCodeSymbol(String CurrencyCodeSymbol) {
            this.CurrencyCodeSymbol = CurrencyCodeSymbol;
        }

        public List<ModelBean> getModel() {
            return Model;
        }

        public void setModel(List<ModelBean> Model) {
            this.Model = Model;
        }

        public static class ModelBean {
            @Override
            public String toString() {
                return "ModelBean{" +
                        "DeliveryTime='" + DeliveryTime + '\'' +
                        ", Cost=" + Cost +
                        ", IsTracking=" + IsTracking +
                        ", ShippingMethod='" + ShippingMethod + '\'' +
                        '}';
            }

            /**
             * DeliveryTime : 7-10
             * Cost : 5
             * IsTracking : false
             * ShippingMethod : SuperSaver
             */

            private String DeliveryTime;
            private String Cost;
            private boolean IsTracking;
            private String ShippingMethod;

            public String getDeliveryTime() {
                return DeliveryTime;
            }

            public void setDeliveryTime(String DeliveryTime) {
                this.DeliveryTime = DeliveryTime;
            }

            public String getCost() {
                return Cost;
            }

            public void setCost(String Cost) {
                this.Cost = Cost;
            }

            public boolean isIsTracking() {
                return IsTracking;
            }

            public void setIsTracking(boolean IsTracking) {
                this.IsTracking = IsTracking;
            }

            public String getShippingMethod() {
                return ShippingMethod;
            }

            public void setShippingMethod(String ShippingMethod) {
                this.ShippingMethod = ShippingMethod;
            }
        }
    }
}
