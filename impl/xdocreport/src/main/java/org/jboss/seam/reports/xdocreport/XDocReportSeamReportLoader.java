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
package org.jboss.seam.reports.xdocreport;

import static org.jboss.seam.solder.reflection.AnnotationInspector.getAnnotation;

import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.jboss.seam.reports.ReportLoader;
import org.jboss.seam.reports.exceptions.ReportException;
import org.jboss.seam.reports.xdocreport.annotations.Via;
import org.jboss.seam.reports.xdocreport.annotations.XDocReport;
import org.jboss.seam.solder.resourceLoader.ResourceLoaderManager;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

@XDocReport
public class XDocReportSeamReportLoader implements ReportLoader
{

   private ResourceLoaderManager resourceLoaderManager;
   private TemplateEngineKind engineKind;

   @Inject
   public XDocReportSeamReportLoader(InjectionPoint injectionPoint, BeanManager beanManager,
            ResourceLoaderManager resourceLoaderManager)
   {
      this.resourceLoaderManager = resourceLoaderManager;
      Via via = getAnnotation(injectionPoint.getAnnotated(), Via.class, beanManager);
      engineKind = (via == null) ? TemplateEngineKind.Velocity : TemplateEngineKind.valueOf(via.value());
   }

   @Override
   public XDocReportSeamReport loadReport(InputStream input) throws ReportException
   {
      IXDocReport report;
      try
      {
         report = XDocReportRegistry.getRegistry().loadReport(input, engineKind);
         return new XDocReportSeamReport(null, report, report.createContext());
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         throw new ReportException(e);
      }
      catch (XDocReportException e)
      {
         throw new ReportException(e);
      }
   }

   @Override
   public XDocReportSeamReport loadReport(String name) throws ReportException
   {
      InputStream resourceAsStream = resourceLoaderManager.getResourceAsStream(name);
      return loadReport(resourceAsStream);
   }

   @Override
   public XDocReportSeamReportDefinition loadReportDefinition(String name) throws ReportException
   {
      InputStream resourceAsStream = resourceLoaderManager.getResourceAsStream(name);
      return loadReportDefinition(resourceAsStream);
   }

   @Override
   public XDocReportSeamReportDefinition loadReportDefinition(InputStream input) throws ReportException
   {
      XDocArchive archive;
      try
      {
         archive = XDocArchive.readZip(input);
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         throw new ReportException(e);
      }
      return new XDocReportSeamReportDefinition(archive, engineKind);
   }

}
