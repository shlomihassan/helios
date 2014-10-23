/*
 * Copyright (c) 2014 Spotify AB.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.spotify.helios.servicescommon;

import com.google.common.collect.ImmutableList;

import io.dropwizard.jetty.ConnectorFactory;
import io.dropwizard.jetty.HttpsConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;

import java.net.InetSocketAddress;
import java.util.Collections;

public class ServiceUtil {
  public static DefaultServerFactory createServerFactory(final InetSocketAddress httpEndpoint,
                                                         final int adminPort,
                                                         final boolean noHttp) {

    final DefaultServerFactory serverFactory = new DefaultServerFactory();
    if (noHttp) {
      serverFactory.setApplicationConnectors(Collections.<ConnectorFactory>emptyList());
      serverFactory.setAdminConnectors(Collections.<ConnectorFactory>emptyList());
    } else {
      final HttpsConnectorFactory serviceConnector = new HttpsConnectorFactory();
      serviceConnector.setPort(httpEndpoint.getPort());
      serviceConnector.setBindHost(httpEndpoint.getHostString());
      setSslConfig(serviceConnector);

      serverFactory.setApplicationConnectors(ImmutableList.<ConnectorFactory>of(serviceConnector));

      final HttpsConnectorFactory adminConnector = new HttpsConnectorFactory();
      adminConnector.setPort(adminPort);
      setSslConfig(adminConnector);
      
      serverFactory.setAdminConnectors(ImmutableList.<ConnectorFactory>of(adminConnector));
    }
    return serverFactory;
  }

  private static void setSslConfig(final HttpsConnectorFactory serviceConnector) {
    serviceConnector.setKeyStorePassword("xxxxxx");
    serviceConnector.setKeyStorePath("/home/drewc/.keystore");
    serviceConnector.setCertAlias("mykey");
    serviceConnector.setValidateCerts(false);
    serviceConnector.setWantClientAuth(true);
  }
}
