package com.itrail.klinikreact.testcontainer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import org.junit.jupiter.api.DisplayName;

public class BackupPostgres {

    @DisplayName("Загрузка данных в бд")
    public static void getRestoreDataBase( int port ) throws Exception {
        List<String> restore = List.of("pg_restore", "--host=localhost", "--port=" + port,"--username=postgres",
                                       "--dbname=Klinika",  "--no-password","--format=c","--verbose",
                                          "./src/main/resources/db/backup/klinika.backup");
        processBuilder( restore );
    }
    
    @DisplayName("Загрузка данных")
    private static void processBuilder( List<String>  commnds ) throws Exception{
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
            System.out.println("Restore Db success!");
        } else {   
            System.out.println("Code >>> " + exitCode);
        }

    }
    
    
}
