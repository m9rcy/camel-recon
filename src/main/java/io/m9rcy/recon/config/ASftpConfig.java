package io.m9rcy.recon.config;

import org.apache.camel.component.file.remote.SftpConfiguration;
import org.apache.camel.component.file.remote.SftpEndpoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.camel.builder.Builder.constant;
import static org.apache.camel.builder.Builder.simple;

@Configuration
public class ASftpConfig {

    private static final String PROCESSOR_NAME_A_SFTP = "a-sftp";
    private static final String CACHE_NAME_A_SFTP_IN_PROGRESS = "a-sftp-in-progress";
    private static final String IDEMPOTENT = "idempotent";
    private static final String FILE_NAME_INCLUDE_PATTERN = "^FILE-TO-PROCESS.*\\.txt$";

    @SuppressWarnings("Duplicates")
    @Bean(name = "aSFTPConfiguration")
    public SftpConfiguration sftpConfiguration(ASftpConfigProperties aSftpConfigProperties) {
        var sftpConfiguration = new SftpConfiguration();
        sftpConfiguration.setHost(aSftpConfigProperties.getHost());
        sftpConfiguration.setPort(Integer.parseInt(aSftpConfigProperties.getPort()));
        sftpConfiguration.setDirectory(aSftpConfigProperties.getDirectory());
        sftpConfiguration.setUsername(aSftpConfigProperties.getUser());
        sftpConfiguration.setPassword(aSftpConfigProperties.getPass());
        sftpConfiguration.setBinary(Boolean.TRUE);
        return sftpConfiguration;
    }

    @Bean(name = "inSFTPEndpoint")
    public SftpEndpoint inSftpEndpoint(@Qualifier("aSFTPConfiguration") SftpConfiguration aSFTPConfiguration) {
        var sftpEndpoint = new SftpEndpoint();
        sftpEndpoint.setConfiguration(aSFTPConfiguration);
        sftpEndpoint.setIdempotent(true);
        sftpEndpoint.setInclude(FILE_NAME_INCLUDE_PATTERN);
        sftpEndpoint.setMove(simple("./archive/${headers.CamelFileName}.${date:now:yyyyMMddHHmmss}.old"));
        return sftpEndpoint;
    }

    @Bean(name = "outSFTPEndpoint")
    public SftpEndpoint outSftpEndpoint(@Qualifier("aSFTPConfiguration") SftpConfiguration aSFTPConfiguration) {
        var sftpEndpoint = new SftpEndpoint();
        var originalDirectory = aSFTPConfiguration.getDirectory();
        sftpEndpoint.setConfiguration(aSFTPConfiguration);
        sftpEndpoint.setFileName(simple("/${headers.CamelFileName}"));
        //sftpEndpoint.setFileName(constant("test.txt"));

        return sftpEndpoint;
    }

}