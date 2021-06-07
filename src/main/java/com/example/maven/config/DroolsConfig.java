package com.example.maven.config;

import org.drools.core.io.impl.ClassPathResource;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class DroolsConfig {

    private final KieServices kieServices = KieServices.Factory.get();

    public KieContainer getKieContainer() {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(new ClassPathResource("cart.drl"));
        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }


}