/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.svn.utils;

import java.sql.DriverManager;
import java.sql.SQLException;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;

/**
 *
 * @author npopov
 */
public class UserFuncTestRunner {

    private static final String keyFile = "C:\\Users\\npopov\\Documents\\npopov\\migrate\\my_files\\sert_2017\\key.pem";
    private static final String txUrl = "svn+cplus://svn2.compassplus.ru/twrbs/";
    private static final String txBranch = "trunk/releases/1.1.40.10.1";
    private static final String txDb = "jdbc:oracle:thin:@10.7.1.55:1521/TERMINALPAB";
    private static final String txSchema = "RBS";
    
    private static final String rxUrl = "svn+cplus://svn2.compassplus.ru/radix";
    private static final String rxBranch = "releases/2.1.13.10.3";
    private static final String rxDb = "jdbc:oracle:thin:@10.77.201.31:1521/x";
    private static final String rxSchema = "RADIX";


    public static void main(String[] args) throws RadixSvnException, SQLException {
        SVNRepositoryAdapter repository = SVNRepositoryAdapter.Factory.newInstance(rxUrl, "", "svn", org.radixware.kernel.common.svn.SVN.getForAuthType(ESvnAuthType.SSH_KEY_FILE), keyFile);
        java.sql.Connection con = DriverManager.getConnection(rxDb, rxSchema, rxSchema);
        final BranchHolderParams branchParams = new BranchHolderParams(repository, rxBranch, null, false);
        UserFuncVerifier instance = new UserFuncVerifier(branchParams, con, ERuntimeEnvironmentType.SERVER) {

            @Override
            public void error(Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void error(String message) {
                System.err.println(message);
            }

            @Override
            public void message(String message) {
                System.out.println(message);
            }
        };
        try {
            boolean result = instance.verify();
            if (!result) {
                System.err.println("Result: there are errors");
            }
        } finally {
            con.close();
        }
    }
}
