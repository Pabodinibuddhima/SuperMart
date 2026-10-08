/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.supermart.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {

    private static final Path CONFIG_FILE =
            Path.of("config", "db.properties");

    private DBConnection() {
    }

    private static Properties loadProperties()
            throws SQLException {

        Properties properties = new Properties();

        if (!Files.isRegularFile(CONFIG_FILE)) {
            throw new SQLException(
                    "Database configuration not found: "
                    + CONFIG_FILE.toAbsolutePath()
            );
        }

        try (InputStream input =
                Files.newInputStream(CONFIG_FILE)) {

            properties.load(input);

        } catch (IOException e) {
            throw new SQLException(
                    "Unable to read database configuration.",
                    e
            );
        }

        return properties;
    }

    public static Connection getConnection()
            throws SQLException {

        Properties properties = loadProperties();

        return testConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }

    public static Connection testConnection(
            String url,
            String username,
            String password
    ) throws SQLException {

        if (url == null || url.isBlank()
                || username == null || username.isBlank()) {
            throw new SQLException(
                    "Database URL and username are required."
            );
        }

        return DriverManager.getConnection(
                url,
                username,
                password == null ? "" : password
        );
    }

    public static void saveConfiguration(
            String url,
            String username,
            String password
    ) throws IOException {

        Properties properties = new Properties();

        properties.setProperty("db.url", url);
        properties.setProperty("db.user", username);
        properties.setProperty(
                "db.password",
                password == null ? "" : password
        );

        Files.createDirectories(
                CONFIG_FILE.toAbsolutePath().getParent()
        );

        try (OutputStream output =
                Files.newOutputStream(CONFIG_FILE)) {

            properties.store(
                    output,
                    "SuperMart Database Configuration"
            );
        }

        // Best-effort restriction on Unix-like systems.
        // Windows uses its own file permissions.
        try {
            Files.setPosixFilePermissions(
                    CONFIG_FILE,
                    java.nio.file.attribute.PosixFilePermissions
                            .fromString("rw-------")
            );
        } catch (UnsupportedOperationException ignored) {
            // Non-POSIX filesystem.
        }
    }

    public static Properties getConfiguration()
            throws SQLException {

        return loadProperties();
    }
}


/*
package com.supermart.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input =
                DBConnection.class.getClassLoader()
                        .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "db.properties file was not found."
                );
            }

            PROPERTIES.load(input);

        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                PROPERTIES.getProperty("db.url"),
                PROPERTIES.getProperty("db.user"),
                PROPERTIES.getProperty("db.password")
        );
    }
}

*/