package org.mo.bots.PizzaBot.data;

import org.mo.bots.PizzaBot.objects.User;

public interface DataProvider {

    User getUserByPhone(String phone);

}
