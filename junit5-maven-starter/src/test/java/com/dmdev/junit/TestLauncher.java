package com.dmdev.junit;

import com.dmdev.junit.service.UserServiceTest;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.io.PrintWriter;

public class TestLauncher {
    public static void main(String[] args) {
        Launcher launcher = LauncherFactory.create();

//        launcher.registerTestExecutionListeners();
//        launcher.registerTestExecutionListeners();

        SummaryGeneratingListener summaryGeneratingListener = new SummaryGeneratingListener();

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
//                .selectors(DiscoverySelectors.selectClass(UserServiceTest.class))
                .selectors(DiscoverySelectors.selectPackage("com.dmdev.junit.service"))
//                .listeners()
                .build();

        launcher.execute(request, summaryGeneratingListener);

        try (PrintWriter printerWriter = new PrintWriter(System.out);) {
            summaryGeneratingListener.getSummary().printTo(printerWriter);
        }
       }
}
