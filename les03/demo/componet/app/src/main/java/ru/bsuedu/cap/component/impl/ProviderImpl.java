package ru.bsuedu.cap.component.impl;

import org.springframework.stereotype.Component;

import ru.bsuedu.cap.component.Provider;

@Component("provider")
public class ProviderImpl  implements Provider{
    

    @Override
    public String getMessage() {
        return "Message from provider";
    }

}
