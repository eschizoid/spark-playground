//package com.simba.cassandra.cassandra.core;
//
//import com.datastax.driver.core.Cluster;
//import com.datastax.driver.core.ConsistencyLevel;
//import com.datastax.driver.core.JdkSSLOptions;
//import com.datastax.driver.core.QueryOptions;
//import com.datastax.driver.core.ResultSet;
//import com.datastax.driver.core.Row;
//import com.datastax.driver.core.Session;
//import com.datastax.driver.core.Cluster.Builder;
//import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
//import com.datastax.driver.core.policies.LatencyAwarePolicy;
//import com.datastax.driver.core.policies.LoadBalancingPolicy;
//import com.datastax.driver.core.policies.RoundRobinPolicy;
//import com.datastax.driver.core.policies.TokenAwarePolicy;
//import com.simba.cassandra.cassandra.commons.SettingsKeys;
//import com.simba.cassandra.cassandra.core.ssl.CDBJDBCTrustManager;
//import com.simba.cassandra.cassandra.exceptions.CDBJDBCMessageKey;
//import com.simba.cassandra.dsi.core.impl.DSIConnection;
//import com.simba.cassandra.dsi.core.impl.DSILogger;
//import com.simba.cassandra.dsi.core.interfaces.IStatement;
//import com.simba.cassandra.dsi.core.utilities.ConnSettingRequestMap;
//import com.simba.cassandra.dsi.core.utilities.ConnSettingResponseMap;
//import com.simba.cassandra.dsi.core.utilities.PropertyUtilities;
//import com.simba.cassandra.dsi.core.utilities.Variant;
//import com.simba.cassandra.dsi.exceptions.BadAuthException;
//import com.simba.cassandra.jdbc.common.CommonCoreUtils;
//import com.simba.cassandra.jdbc.common.ProductInfoHandler;
//import com.simba.cassandra.support.ILogger;
//import com.simba.cassandra.support.LogUtilities;
//import com.simba.cassandra.support.Warning;
//import com.simba.cassandra.support.WarningCode;
//import com.simba.cassandra.support.exceptions.ErrorException;
//import java.io.FileInputStream;
//import java.security.KeyStore;
//import java.security.SecureRandom;
//import java.util.Iterator;
//import java.util.List;
//import javax.net.ssl.KeyManager;
//import javax.net.ssl.KeyManagerFactory;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;
//
//public class CDBJDBCConnection extends DSIConnection {
//    private static int s_connectionID = 0;
//    private static final String LOG_PREFIX_NAME = "CassandraJDBC_connection_";
//    private final String[] DEFAULT_SSL_CIPHER_SUITES = new String[]{"TLS_RSA_WITH_AES_128_CBC_SHA", "TLS_RSA_WITH_AES_256_CBC_SHA"};
//    private CDBJDBCConnectionSettings m_settings = null;
//    protected ILogger m_log;
//    private Cluster m_cluster;
//    private Session m_session;
//    private ProductInfoHandler m_productInfoHandler;
//
//    public CDBJDBCConnection(CDBJDBCEnvironment var1) throws ErrorException {
//        super(var1);
//        LogUtilities.logFunctionEntrance(this.getConnectionLog(), new Object[]{var1});
//        ++s_connectionID;
//        String var2 = "1.1";
//        this.m_productInfoHandler = new ProductInfoHandler("SimbaApacheCassandraJDBCDriver.lic", "Simba Apache Cassandra JDBC Driver", var2, CDBJDBCDriver.class);
//    }
//
//    public void close() {
//        LogUtilities.logFunctionEntrance(this.getConnectionLog(), new Object[0]);
//        if (null != this.m_cluster) {
//            this.m_cluster.close();
//        }
//
//    }
//
//    public void connect(ConnSettingRequestMap var1) throws ErrorException, BadAuthException {
//        CommonCoreUtils.logConnectionFunctionEntrance(this.getConnectionLog(), var1, 1, 1, 2, 1002);
//        this.getConnectionSettings(var1);
//        Builder var2 = Cluster.builder();
//        var2.addContactPoint(this.m_settings.m_host);
//        var2.withPort(this.m_settings.m_port);
//        QueryOptions var3 = new QueryOptions();
//        var3.setConsistencyLevel(this.m_settings.m_tunableConsistency);
//        var2.withQueryOptions(var3);
//        if (1 == this.m_settings.m_authMech) {
//            var2.withCredentials(this.m_settings.m_uid, this.m_settings.m_pwd);
//        }
//
//        if (0 != this.m_settings.m_sslMode) {
//            var2.withSSL(JdkSSLOptions.builder().withSSLContext(this.getSSLContext()).withCipherSuites(this.DEFAULT_SSL_CIPHER_SUITES).build());
//        }
//
//        Object var4 = null;
//        if (0 == this.m_settings.m_loadBalancingPolicy) {
//            var4 = DCAwareRoundRobinPolicy.builder().build();
//        } else {
//            var4 = new RoundRobinPolicy();
//        }
//
//        if (this.m_settings.m_enableTokenAware) {
//            TokenAwarePolicy var5 = new TokenAwarePolicy((LoadBalancingPolicy)var4);
//            var4 = var5;
//        }
//
//        if (this.m_settings.m_enableLatencyAware) {
//            com.datastax.driver.core.policies.LatencyAwarePolicy.Builder var8 = LatencyAwarePolicy.builder((LoadBalancingPolicy)var4);
//            var4 = var8.build();
//        }
//
//        var2.withLoadBalancingPolicy((LoadBalancingPolicy)var4);
//        this.m_cluster = var2.build();
//
//        try {
//            if (null != this.m_settings.m_defaultKeyspace) {
//                this.m_session = this.m_cluster.connect(this.m_settings.m_defaultKeyspace);
//            } else {
//                this.m_session = this.m_cluster.connect();
//            }
//        } catch (Exception var7) {
//            ErrorException var6 = CDBJDBCDriver.s_DriverMessages.createGeneralException(CDBJDBCMessageKey.CONN_GENERAL_ERR.name(), var7.getMessage());
//            var6.initCause(var7);
//            throw var6;
//        }
//
//        this.setDefaultProperties();
//    }
//
//    public IStatement createStatement() throws ErrorException {
//        LogUtilities.logFunctionEntrance(this.getConnectionLog(), new Object[0]);
//        return new CDBJDBCStatement(this);
//    }
//
//    public void disconnect() throws ErrorException {
//        LogUtilities.logFunctionEntrance(this.getConnectionLog(), new Object[0]);
//        if (null != this.m_cluster) {
//            this.m_cluster.close();
//        }
//
//    }
//
//    protected void doReset() throws ErrorException {
//        LogUtilities.logFunctionEntrance(this.getConnectionLog(), new Object[0]);
//    }
//
//    public ILogger getConnectionLog() {
//        if (null == this.m_log) {
//            this.m_log = new DSILogger("CassandraJDBC_connection_" + Integer.toString(s_connectionID));
//            this.m_log.setLocale(this.getLocale());
//        }
//
//        return this.m_log;
//    }
//
//    public CDBJDBCConnectionSettings getConnectionSettings() {
//        return this.m_settings;
//    }
//
//    public Session getSession() {
//        return this.m_session;
//    }
//
//    public ConnSettingResponseMap updateConnectionSettings(ConnSettingRequestMap var1) throws BadAuthException, ErrorException {
//        LogUtilities.logFunctionEntrance(this.getConnectionLog(), new Object[0]);
//        ConnSettingResponseMap var2 = new ConnSettingResponseMap();
//        if (null == this.m_settings) {
//            this.getConnectionSettings(var1);
//        }
//
//        Iterator var3 = CDBJDBCPropertyKey.getRequiredKeys(this.m_settings).iterator();
//
//        String var4;
//        while(var3.hasNext()) {
//            var4 = (String)var3.next();
//            this.verifyRequiredSetting(var4, var1, var2);
//        }
//
//        var3 = CDBJDBCPropertyKey.getOptionalKeys(this.m_settings).iterator();
//
//        while(var3.hasNext()) {
//            var4 = (String)var3.next();
//            this.verifyOptionalSetting(var4, var1, var2);
//        }
//
//        return var2;
//    }
//
//    private void getConnectionSettings(ConnSettingRequestMap var1) throws ErrorException {
//        this.m_settings = new CDBJDBCConnectionSettings();
//        Variant var2 = null;
//        this.m_settings.m_batchLimit = SettingsKeys.CDB_DEFAULT_BATCH_LIMIT;
//        this.m_settings.m_batchType = SettingsKeys.CDB_DEFAULT_BATCH_TYPE;
//        this.m_settings.m_host = this.getRequiredSetting("Host", var1).getString();
//        this.m_settings.m_port = 9042;
//        var2 = this.getOptionalSetting("Port", var1);
//        boolean var3;
//        int var4;
//        String[] var5;
//        StringBuilder var24;
//        if (null != var2) {
//            var3 = false;
//
//            try {
//                var4 = var2.getInt();
//                if (1 <= var4 && 65535 >= var4) {
//                    this.m_settings.m_port = var4;
//                } else {
//                    var3 = true;
//                }
//            } catch (Exception var23) {
//                var3 = true;
//            }
//
//            if (var3) {
//                var24 = new StringBuilder();
//                var24.append("numbers from 1 to ");
//                var24.append(65535);
//                var24.append(". Falling back to default value ");
//                var24.append(this.m_settings.m_port);
//                var5 = new String[]{var2.getString(), "Port", var24.toString()};
//                this.postWarning(var5);
//            }
//        }
//
//        var2 = this.getOptionalSetting("AuthMech", var1);
//        if (null != var2) {
//            var3 = false;
//
//            try {
//                var4 = var2.getInt();
//                if (SettingsKeys.AUTH_MECHS.contains(var4)) {
//                    this.m_settings.m_authMech = var4;
//                    if (1 == var4) {
//                        this.m_settings.m_uid = this.getRequiredSetting("UID", var1).getString();
//                        this.m_settings.m_pwd = this.getRequiredSetting("PWD", var1).getString();
//                    }
//                } else {
//                    var3 = true;
//                }
//            } catch (Exception var12) {
//                var3 = true;
//            }
//
//            if (var3) {
//                var24 = new StringBuilder();
//                var24.append("0 for authentication off, 1 for authentication on.");
//                var24.append(" Falling back to default value ");
//                var24.append(this.m_settings.m_authMech);
//                var5 = new String[]{var2.getString(), "AuthMech", var24.toString()};
//                this.postWarning(var5);
//            }
//        }
//
//        var2 = this.getOptionalSetting("BinaryColumnLength", var1);
//        if (null != var2) {
//            var3 = false;
//
//            try {
//                var4 = var2.getInt();
//                if (67108864 >= var4 && 0 != var4) {
//                    if (255 > var4) {
//                        this.m_settings.m_binaryColumnLength = 255;
//                        var3 = true;
//                    } else {
//                        this.m_settings.m_binaryColumnLength = var4;
//                    }
//                } else {
//                    this.m_settings.m_binaryColumnLength = 67108864;
//                    var3 = true;
//                }
//            } catch (Exception var22) {
//                var3 = true;
//            }
//
//            if (var3) {
//                var24 = new StringBuilder();
//                var24.append("numbers from ");
//                var24.append(255);
//                var24.append(" to ");
//                var24.append(67108864);
//                var24.append(" or 0 for maximum column length. Falling back to value ");
//                var24.append(this.m_settings.m_binaryColumnLength);
//                var5 = new String[]{var2.getString(), "BinaryColumnLength", var24.toString()};
//                this.postWarning(var5);
//            }
//        }
//
//        var2 = this.getOptionalSetting("DecimalColumnScale", var1);
//        if (null != var2) {
//            var3 = false;
//
//            try {
//                var4 = var2.getInt();
//                if (0 < var4 && 38 >= var4) {
//                    this.m_settings.m_decimalColumnScale = var4;
//                } else {
//                    var3 = true;
//                }
//            } catch (Exception var21) {
//                var3 = true;
//            }
//
//            if (var3) {
//                var24 = new StringBuilder();
//                var24.append("numbers from 1 to ");
//                var24.append(38);
//                var24.append(". Falling back to default value ");
//                var24.append(this.m_settings.m_decimalColumnScale);
//                var5 = new String[]{var2.getString(), "DecimalColumnScale", var24.toString()};
//                this.postWarning(var5);
//            }
//        }
//
//        var2 = this.getOptionalSetting("DefaultKeyspace", var1);
//        if (null != var2) {
//            this.m_settings.m_defaultKeyspace = var2.getString();
//        }
//
//        var2 = this.getOptionalSetting("EnableCaseSensitive", var1);
//        if (null != var2) {
//            var3 = false;
//
//            try {
//                var4 = var2.getInt();
//                if (0 != var4 && 1 != var4) {
//                    var3 = true;
//                } else {
//                    this.m_settings.m_enableCaseSensitive = var4 == 1;
//                }
//            } catch (Exception var20) {
//                var3 = true;
//            }
//
//            if (var3) {
//                var24 = new StringBuilder();
//                var24.append("0 to disable case sensitive, ");
//                var24.append("1 to enable case sensitive.");
//                var24.append(" Falling back to default value ");
//                var24.append(this.m_settings.m_enableCaseSensitive ? 1 : 0);
//                var5 = new String[]{var2.getString(), "EnableCaseSensitive", var24.toString()};
//                this.postWarning(var5);
//            }
//        }
//
//        var2 = this.getOptionalSetting("EnableLatencyAware", var1);
//        if (null != var2) {
//            var3 = false;
//
//            try {
//                var4 = var2.getInt();
//                if (0 != var4 && 1 != var4) {
//                    var3 = true;
//                } else {
//                    this.m_settings.m_enableLatencyAware = var4 == 1;
//                }
//            } catch (Exception var19) {
//                var3 = true;
//            }
//
//            if (var3) {
//                var24 = new StringBuilder();
//                var24.append("0 to disable the use of LatencyAwarePolicy, ");
//                var24.append("1 to use LatencyAwarePolicy as the parent policy.");
//                var24.append(" Falling back to default value ");
//                var24.append(this.m_settings.m_enableLatencyAware ? 1 : 0);
//                var5 = new String[]{var2.getString(), "EnableLatencyAware", var24.toString()};
//                this.postWarning(var5);
//            }
//        }
//
//        var2 = this.getOptionalSetting("EnablePaging", var1);
//        int var6;
//        String[] var7;
//        StringBuilder var25;
//        boolean var26;
//        if (null != var2) {
//            var3 = false;
//
//            try {
//                var4 = var2.getInt();
//                if (0 != var4 && 1 != var4) {
//                    var3 = true;
//                } else {
//                    this.m_settings.m_enablePaging = var4 == 1;
//                    if (this.m_settings.m_enablePaging) {
//                        var2 = this.getOptionalSetting("RowsPerPage", var1);
//                        if (null != var2) {
//                            var26 = false;
//
//                            try {
//                                var6 = var2.getInt();
//                                if (0 < var6 && 1000000 >= var6) {
//                                    this.m_settings.m_rowsPerPage = var6;
//                                } else if (1000000 < var6) {
//                                    this.m_settings.m_rowsPerPage = 1000000;
//                                    var26 = true;
//                                } else {
//                                    var26 = true;
//                                }
//                            } catch (Exception var17) {
//                                var26 = true;
//                            }
//
//                            if (var26) {
//                                var25 = new StringBuilder();
//                                var25.append("numbers from 1 to ");
//                                var25.append(1000000);
//                                var25.append(". Falling back to value ");
//                                var25.append(this.m_settings.m_rowsPerPage);
//                                var7 = new String[]{var2.getString(), "RowsPerPage", var25.toString()};
//                                this.postWarning(var7);
//                            }
//                        }
//                    }
//                }
//            } catch (Exception var18) {
//                var3 = true;
//            }
//
//            if (var3) {
//                var24 = new StringBuilder();
//                var24.append("0 to disable paging, 1 to enable paging.");
//                var24.append(" Falling back to default value ");
//                var24.append(this.m_settings.m_enablePaging ? 1 : 0);
//                var5 = new String[]{var2.getString(), "EnablePaging", var24.toString()};
//                this.postWarning(var5);
//            }
//        }
//
//        var2 = this.getOptionalSetting("EnableTokenAware", var1);
//        if (null != var2) {
//            var3 = false;
//
//            try {
//                var4 = var2.getInt();
//                if (0 != var4 && 1 != var4) {
//                    var3 = true;
//                } else {
//                    this.m_settings.m_enableTokenAware = var4 == 1;
//                }
//            } catch (Exception var16) {
//                var3 = true;
//            }
//
//            if (var3) {
//                var24 = new StringBuilder();
//                var24.append("0 to disable the use of TokenAwarePolicy, ");
//                var24.append("1 to use TokenAwarePolicy as the parent policy.");
//                var24.append(" Falling back to default value ");
//                var24.append(this.m_settings.m_enableTokenAware ? 1 : 0);
//                var5 = new String[]{var2.getString(), "EnableTokenAware", var24.toString()};
//                this.postWarning(var5);
//            }
//        }
//
//        var2 = this.getOptionalSetting("LoadBalancingPolicy", var1);
//        if (null != var2) {
//            var3 = false;
//
//            try {
//                var4 = var2.getInt();
//                if (SettingsKeys.LOAD_BALANCING_POLICY.contains(var4)) {
//                    this.m_settings.m_loadBalancingPolicy = var4;
//                } else {
//                    var3 = true;
//                }
//            } catch (Exception var11) {
//                var3 = true;
//            }
//
//            if (var3) {
//                var24 = new StringBuilder();
//                var24.append("0 for DCAwareRoundRobinPolicy, 1 for RoundRobinPolicy.");
//                var24.append(" Falling back to default value ");
//                var24.append(this.m_settings.m_loadBalancingPolicy);
//                var5 = new String[]{var2.getString(), "LoadBalancingPolicy", var24.toString()};
//                this.postWarning(var5);
//            }
//        }
//
//        var2 = this.getOptionalSetting("QueryMode", var1);
//        if (null != var2) {
//            var3 = false;
//
//            try {
//                var4 = var2.getInt();
//                if (SettingsKeys.QUERY_MODES.contains(var4)) {
//                    this.m_settings.m_queryMode = var4;
//                } else {
//                    var3 = true;
//                }
//            } catch (Exception var10) {
//                var3 = true;
//            }
//
//            if (var3) {
//                var24 = new StringBuilder();
//                var24.append("0 for SQL with CQL fallback, 1 for CQL.");
//                var24.append(" Falling back to default value ");
//                var24.append(this.m_settings.m_queryMode);
//                var5 = new String[]{var2.getString(), "QueryMode", var24.toString()};
//                this.postWarning(var5);
//            }
//        }
//
//        var2 = this.getOptionalSetting("SSLMode", var1);
//        if (null != var2) {
//            int var30 = this.m_settings.m_sslMode;
//            boolean var28 = false;
//
//            try {
//                var30 = var2.getInt();
//            } catch (Exception var9) {
//                var28 = true;
//            }
//
//            if (SettingsKeys.SSL_MODES.contains(var30)) {
//                this.m_settings.m_sslMode = var30;
//                if (0 != this.m_settings.m_sslMode) {
//                    var2 = this.getOptionalSetting("SSLTruststorePath", var1);
//                    if (null != var2) {
//                        this.m_settings.m_sslTruststorePath = var2.getString();
//                    }
//
//                    var2 = this.getOptionalSetting("UseSslIdentityCheck", var1);
//                    if (null != var2) {
//                        var26 = false;
//
//                        try {
//                            var6 = var2.getInt();
//                            if (0 != var6 && 1 != var6) {
//                                var26 = true;
//                            } else {
//                                this.m_settings.m_sslIdentityCheck = var6 == 1;
//                            }
//                        } catch (Exception var15) {
//                            var26 = true;
//                        }
//
//                        if (var26) {
//                            var25 = new StringBuilder();
//                            var25.append("0 to disable SSL identity check, ");
//                            var25.append("1 to enable SSL identity check.");
//                            var25.append(" Falling back to default value ");
//                            var25.append(this.m_settings.m_sslIdentityCheck ? 1 : 0);
//                            var7 = new String[]{var2.getString(), "UseSslIdentityCheck", var25.toString()};
//                            this.postWarning(var7);
//                        }
//                    }
//
//                    if (2 == this.m_settings.m_sslMode) {
//                        var2 = this.getOptionalSetting("SSLKeystorePath", var1);
//                        if (null != var2) {
//                            this.m_settings.m_sslKeystorePath = var2.getString();
//                        }
//
//                        var2 = this.getOptionalSetting("SSLTruststorePwd", var1);
//                        if (null != var2) {
//                            this.m_settings.m_sslTruststorePwd = var2.getString();
//                        }
//
//                        var2 = this.getOptionalSetting("SSLKeystorePwd", var1);
//                        if (null != var2) {
//                            this.m_settings.m_sslKeystorePwd = var2.getString();
//                        }
//                    }
//                }
//            } else {
//                var28 = true;
//            }
//
//            if (var28) {
//                StringBuilder var29 = new StringBuilder();
//                var29.append("0 to disable SSL, ");
//                var29.append("1 for one-way SSL, ");
//                var29.append("2 for two-way SSL.");
//                var29.append(" Falling back to default value ");
//                var29.append(this.m_settings.m_sslMode);
//                String[] var27 = new String[]{var2.getString(), "SSLMode", var29.toString()};
//                this.postWarning(var27);
//            }
//        }
//
//        var2 = this.getOptionalSetting("StringColumnLength", var1);
//        if (null != var2) {
//            var3 = false;
//
//            try {
//                var4 = var2.getInt();
//                if (67108864 >= var4 && 0 != var4) {
//                    if (255 > var4) {
//                        this.m_settings.m_stringColumnLength = 255;
//                        var3 = true;
//                    } else {
//                        this.m_settings.m_stringColumnLength = var4;
//                    }
//                } else {
//                    this.m_settings.m_stringColumnLength = 67108864;
//                    var3 = true;
//                }
//            } catch (Exception var14) {
//                var3 = true;
//            }
//
//            if (var3) {
//                var24 = new StringBuilder();
//                var24.append("numbers from ");
//                var24.append(255);
//                var24.append(" to ");
//                var24.append(67108864);
//                var24.append(" or 0 for maximum column length.");
//                var24.append(" Falling back to value ");
//                var24.append(this.m_settings.m_stringColumnLength);
//                var5 = new String[]{var2.getString(), "StringColumnLength", var24.toString()};
//                this.postWarning(var5);
//            }
//        }
//
//        var2 = this.getOptionalSetting("TunableConsistency", var1);
//        if (null != var2) {
//            var3 = false;
//
//            try {
//                var4 = var2.getInt();
//                if (SettingsKeys.TUNABLE_CONSISTENCY_LEVELS.contains(var4)) {
//                    this.m_settings.m_tunableConsistency = this.getConsistencyLevel(var4);
//                } else {
//                    var3 = true;
//                }
//            } catch (Exception var8) {
//                var3 = true;
//            }
//
//            if (var3) {
//                var24 = new StringBuilder();
//                var24.append("0 for consistency ANY, 1 for consistency ONE, ");
//                var24.append("2 for consistency TWO, 3 for consistency THREE, ");
//                var24.append("4 for consistency QUORUM, 5 for consistency ALL, ");
//                var24.append("6 for consistency LOCAL_QUORUM, 7 for consistency EACH_QUORUM, ");
//                var24.append("10 for consistency LOCAL_ONE.");
//                var24.append(" Falling back to default value ");
//                var24.append(this.m_settings.m_tunableConsistency);
//                var5 = new String[]{var2.getString(), "TunableConsistency", var24.toString()};
//                this.postWarning(var5);
//            }
//        }
//
//        var2 = this.getOptionalSetting("VTTableNameSeparator", var1);
//        if (null != var2) {
//            this.m_settings.m_vtTableNameSeparator = var2.getString();
//        }
//
//        var2 = this.getOptionalSetting("EnableNullInsert", var1);
//        if (null != var2) {
//            var3 = false;
//
//            try {
//                var4 = var2.getInt();
//                if (0 != var4 && 1 != var4) {
//                    var3 = true;
//                } else {
//                    this.m_settings.m_enableNullInsert = var4 == 1;
//                }
//            } catch (Exception var13) {
//                var3 = true;
//            }
//
//            if (var3) {
//                var24 = new StringBuilder();
//                var24.append("0 to disable Null inserts, ");
//                var24.append("1 to enable Null inserts.");
//                var24.append(" Falling back to default value ");
//                var24.append(this.m_settings.m_enableNullInsert ? 1 : 0);
//                var5 = new String[]{var2.getString(), "EnableNullInsert", var24.toString()};
//                this.postWarning(var5);
//            }
//        }
//
//    }
//
//    private ConsistencyLevel getConsistencyLevel(int var1) {
//        switch(var1) {
//            case 0:
//                return ConsistencyLevel.ANY;
//            case 1:
//                return ConsistencyLevel.ONE;
//            case 2:
//                return ConsistencyLevel.TWO;
//            case 3:
//                return ConsistencyLevel.THREE;
//            case 4:
//                return ConsistencyLevel.QUORUM;
//            case 5:
//                return ConsistencyLevel.ALL;
//            case 6:
//                return ConsistencyLevel.LOCAL_QUORUM;
//            case 7:
//                return ConsistencyLevel.EACH_QUORUM;
//            case 8:
//            case 9:
//            default:
//                return null;
//            case 10:
//                return ConsistencyLevel.LOCAL_ONE;
//        }
//    }
//
//    private String getReleaseVersion() throws ErrorException {
//        String var1 = null;
//        this.m_cluster.getConfiguration().getQueryOptions().setConsistencyLevel(ConsistencyLevel.ALL);
//        Session var2 = this.m_cluster.newSession();
//        ResultSet var3 = var2.execute("select release_version from system.local");
//        List var4 = var3.all();
//        if (var4.size() == 1) {
//            var1 = ((Row)var4.get(0)).getString(0);
//            var2.close();
//            this.m_cluster.getConfiguration().getQueryOptions().setConsistencyLevel(this.m_settings.m_tunableConsistency);
//            return var1;
//        } else {
//            throw CDBJDBCDriver.s_DriverMessages.createGeneralException(CDBJDBCMessageKey.DRIVER_DEFAULT_PROP_ERR.name());
//        }
//    }
//
//    private SSLContext getSSLContext() throws ErrorException {
//        SSLContext var1 = null;
//
//        try {
//            var1 = SSLContext.getInstance("TLS");
//            TrustManager[] var2 = null;
//            char[] var14 = null;
//            String var4 = this.m_settings.m_sslTruststorePath;
//            String var5 = this.m_settings.m_sslTruststorePwd;
//            if (null != var4) {
//                FileInputStream var6 = new FileInputStream(var4);
//                KeyStore var7 = KeyStore.getInstance(KeyStore.getDefaultType());
//                if (null != var5) {
//                    var14 = var5.toCharArray();
//                }
//
//                var7.load(var6, var14);
//            }
//
//            CDBJDBCTrustManager var15;
//            if (!this.m_settings.m_sslIdentityCheck) {
//                var15 = new CDBJDBCTrustManager();
//                var2 = new TrustManager[]{var15};
//            } else {
//                var15 = new CDBJDBCTrustManager(this.m_settings.m_host);
//                var2 = new TrustManager[]{var15};
//            }
//
//            KeyManager[] var16 = null;
//            char[] var17 = null;
//            if (this.m_settings.m_sslMode == 2) {
//                String var8 = this.m_settings.m_sslKeystorePath;
//                String var9 = this.m_settings.m_sslKeystorePwd;
//                if (null != var8) {
//                    FileInputStream var10 = new FileInputStream(var8);
//                    KeyStore var11 = KeyStore.getInstance(KeyStore.getDefaultType());
//                    if (null != var9) {
//                        var17 = var9.toCharArray();
//                    }
//
//                    var11.load(var10, var17);
//                    KeyManagerFactory var12 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//                    var12.init(var11, var17);
//                    var16 = var12.getKeyManagers();
//                }
//            }
//
//            var1.init(var16, var2, new SecureRandom());
//            return var1;
//        } catch (Exception var13) {
//            ErrorException var3 = CDBJDBCDriver.s_DriverMessages.createGeneralException(CDBJDBCMessageKey.CONN_GENERAL_ERR.name(), "Error loading the truststore or keystore");
//            var3.initCause(var13);
//            LogUtilities.logError(var3, this.m_log);
//            throw var3;
//        }
//    }
//
//    private void postWarning(String[] var1) {
//        this.getWarningListener().postWarning(new Warning(WarningCode.INVALID_CONNECTION_STRING_ATTRIBUTE, 101, CDBJDBCMessageKey.CONN_INVALID_PROPERTY_VALUE.name(), var1));
//    }
//
//    private void setDefaultProperties() throws ErrorException {
//        String var1 = "N";
//        char var2 = 0;
//
//        try {
//            PropertyUtilities.setCatalogSupport(this, true);
//            PropertyUtilities.setReadOnly(this, false);
//            PropertyUtilities.setSavepointSupport(this, false);
//            PropertyUtilities.setSchemaSupport(this, true);
//            PropertyUtilities.setStoredProcedureSupport(this, false);
//            this.setProperty(101, new Variant(0, "CassandraJDBC"));
//            this.setProperty(41, new Variant(0, "Cassandra"));
//            String var3 = this.getReleaseVersion();
//            this.setProperty(42, new Variant(0, var3));
//            if (null != this.m_settings.m_uid) {
//                this.setProperty(139, new Variant(0, this.m_settings.m_uid));
//            }
//
//            this.setProperty(87, new Variant(0, "N"));
//            if (this.m_settings.m_enableCaseSensitive) {
//                this.setProperty(57, new Variant(2, '\u0003'));
//            } else {
//                this.setProperty(57, new Variant(2, '\u0002'));
//            }
//
//            this.setProperty(96, new Variant(0, ""));
//            this.setProperty(45, new Variant(2, '\u0000'));
//            this.setProperty(137, new Variant(2, '\u0000'));
//            this.setProperty(26, new Variant(2, '\u0000'));
//            this.setProperty(65, new Variant(3, new Long(67108864L)));
//            this.setProperty(81, new Variant(2, '\u0000'));
//            this.setProperty(34, new Variant(4, 1L));
//            this.setProperty(33, new Variant(4, 1L));
//            if (this.m_settings.m_queryMode == 1) {
//                this.setProperty(104, new Variant(3, 0L));
//                this.setProperty(90, new Variant(3, 0L));
//                this.setProperty(135, new Variant(3, 0L));
//                this.setProperty(131, new Variant(3, 0L));
//                this.setProperty(6, new Variant(3, 576L));
//                this.setProperty(103, new Variant(2, var2));
//                this.setProperty(105, new Variant(2, var2));
//                this.setProperty(55, new Variant(0, var1));
//                this.setProperty(92, new Variant(2, var2));
//                this.setProperty(56, new Variant(2, var2));
//                this.setProperty(63, new Variant(0, var1));
//                this.setProperty(138, new Variant(2, var2));
//            }
//
//        } catch (Exception var4) {
//            throw CDBJDBCDriver.s_DriverMessages.createGeneralException(CDBJDBCMessageKey.CONN_DEFAULT_PROP_ERR.name(), var4);
//        }
//    }
//}
