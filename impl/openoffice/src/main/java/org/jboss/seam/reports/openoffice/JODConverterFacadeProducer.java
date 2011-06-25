/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.reports.openoffice;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import org.artofsolving.jodconverter.office.OfficeManager;
import org.jboss.seam.reports.openoffice.framework.JODConverterFacade;

public class JODConverterFacadeProducer {

    @Produces
    @ApplicationScoped
    @OOReports
    public JODConverterFacade createConverterFacade(@OOReports OfficeManager officeManager) {
        JODConverterFacade facade = new JODConverterFacade(officeManager);
        facade.start();
        return facade;
    }

    public void destroyFacade(@Disposes @OOReports JODConverterFacade facade) {
        facade.stop();
    }

}
