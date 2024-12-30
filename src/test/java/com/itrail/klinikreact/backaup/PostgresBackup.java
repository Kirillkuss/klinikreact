package com.itrail.klinikreact.backaup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

@Disabled
@DisplayName( "Добавление и загурзка klinika.backup")
public class PostgresBackup {
    
        ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Создание klinika.backup")
    public void firstTestDump() throws Exception{
        List<String> dump = List.of("pg_dump", "--host=localhost","--port=5432","--username=" + "postgres",
                                       "--file=" + "./src/main/resources/db/backup/klinika.backup","--no-password",
                                       "--format=c","--encoding=UTF8","--verbose","Klinika");
        processBuilder( dump );
        
    }

    @Test
    @DisplayName("Загрузка в бд klinika.backup")
    public void firstTestRestore() throws Exception {
        List<String> restore = List.of("pg_restore", "--host=localhost", "--port=5432","--username=postgres",
                                       "--dbname=Klinika",  "--no-password","--format=c","--verbose",
                                          "./src/main/resources/db/backup/klinika.backup");
        processBuilder( restore );
    }

    private void processBuilder( List<String>  commnds ) throws Exception{
        ProcessBuilder processBuilder = new ProcessBuilder( commnds );
        processBuilder.environment().put("PGPASSWORD", "admin");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Success");
        } else {   
            System.out.println("Code >>> " + exitCode);
        }

    }
}
