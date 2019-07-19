package se.lexicon.account.component.service;

import com.so4it.gs.rpc.Service;
import com.so4it.gs.rpc.ServiceBindingType;
import com.so4it.gs.rpc.ServiceProvider;

@ServiceProvider
public interface AccountComponentServiceProvider {

    @Service(value = ServiceBindingType.GS_REMOTING, name = AccountComponentService.DEFAULT_BEAN_NAME)
    AccountComponentService getInvoiceComponentService();


    @Service(value = ServiceBindingType.GS_REMOTING, name = OrderComponentService.DEFAULT_BEAN_NAME)
    OrderComponentService getOrderComponentService();
}
