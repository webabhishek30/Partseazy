package com.partseazy.android.ui.fragments.fos;

import com.partseazy.android.ui.model.agentapp.Retailer;

/**
 * Created by naveen on 13/2/17.
 */

public class AgentSingleton {

    private static AgentSingleton agentSingleton = new AgentSingleton( );
    private boolean isAssociatedRetailer=false;
    private Retailer retailer = null;

    private AgentSingleton() { }

    public static AgentSingleton getInstance( ) {
        return agentSingleton;
    }


    public void setAssociateRetailer( boolean isAssociatedRetailer) {
        this.isAssociatedRetailer = isAssociatedRetailer;
    }

    public boolean getAssociateRetailerStatus( ) {
        return  isAssociatedRetailer;
    }

    public void setRetailerData(Retailer retailerData)
    {
        this.retailer = retailerData;
    }
    public Retailer getRetailerData()
    {
        return  retailer;
    }


}
