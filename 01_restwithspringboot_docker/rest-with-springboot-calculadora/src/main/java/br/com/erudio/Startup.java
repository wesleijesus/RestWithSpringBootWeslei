package br.com.erudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.erudio.config.FileStorageConfig;

@SpringBootApplication
@EnableConfigurationProperties({
	FileStorageConfig.class
})
@EnableAutoConfiguration /*Permitir que o app seja automaticamente carregDO BASEADO NO JARS, SEMPRE E FEITO DEPOIS DO BEANS*/

@ComponentScan /*sCANEAR OS PACOTES E ACHAR OS ARQUIVOS DE CONFIGURACAO*/
public class Startup {
	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);
		String result = bCryptPasswordEncoder.encode("admin123");
		System.out.println("My hash " + result);
	}
}
