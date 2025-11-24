package com.jpribeiro.DAO;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;

import static org.mockito.Mockito.*;

class DatabaseTest {
    @Test
    void testGetConnection() throws Exception {
        try (MockedStatic<DriverManager> mocked = mockStatic(DriverManager.class)) {

            Connection fakeConn = mock(Connection.class);
            mocked.when(() -> DriverManager.getConnection("jdbc:sqlite:database.db"))
                    .thenReturn(fakeConn);

            Connection conn = Database.getConnection();

            mocked.verify(() -> DriverManager.getConnection("jdbc:sqlite:database.db"));
            assert conn == fakeConn;
        }
    }

    @Test
    void testInitExecutaCriacaoDeTabelas() throws Exception {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);

        try (MockedStatic<Database> mockedDb = mockStatic(Database.class, CALLS_REAL_METHODS)) {
            mockedDb.when(Database::getConnection).thenReturn(conn);
            when(conn.createStatement()).thenReturn(stmt);

            Database.init();

            verify(stmt, atLeastOnce()).execute(anyString());
        }
    }
}