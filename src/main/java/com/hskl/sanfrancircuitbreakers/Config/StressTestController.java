package com.hskl.sanfrancircuitbreakers.Config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class StressTestController {

    @GetMapping("/stress-cpu")
    public ResponseEntity<String> stressCpu() {
        int numCores = Runtime.getRuntime().availableProcessors();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numCores * 2; i++) { // alle Kerne doppelt belegen
            Thread t = new Thread(() -> {
                while (true) {
                    Math.pow(Math.random(), Math.random()); // sinnlose Berechnung
                }
            });
            t.setDaemon(true); // falls du abbrechen willst
            t.start();
            threads.add(t);
        }

        return ResponseEntity.ok("🔥 CPU-Stress gestartet mit " + threads.size() + " Threads.");
    }

    /**
     * Verlangsamt gezielt die Antwortzeit durch Sleep.
     */
    @GetMapping("/slow")
    public ResponseEntity<String> slowDown() throws InterruptedException {
        Thread.sleep(5000); // 5 Sekunden künstliche Verzögerung
        return ResponseEntity.ok("Antwort kam mit Delay.");
    }

    /**
     * Simuliert hohen Speicherverbrauch – Achtung: Kann OutOfMemory erzeugen.
     */
    @GetMapping("/memory")
    public ResponseEntity<String> memoryLoad() {
        List<byte[]> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(new byte[10 * 1024 * 1024]); // 10 MB * 1000 = 10 GB
        }
        return ResponseEntity.ok("Memory-Load abgeschlossen.");
    }
}
