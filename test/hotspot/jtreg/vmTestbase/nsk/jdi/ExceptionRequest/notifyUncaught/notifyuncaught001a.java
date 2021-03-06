/*
 * Copyright (c) 2001, 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package nsk.jdi.ExceptionRequest.notifyUncaught;

import nsk.share.*;
import nsk.share.jdi.*;

/**
 * This class is used as debuggee application for the notifyuncaught001 JDI test.
 */

public class notifyuncaught001a {

    //----------------------------------------------------- templete section

    static final int PASSED = 0;
    static final int FAILED = 2;
    static final int PASS_BASE = 95;

    static ArgumentHandler argHandler;
    static Log log;

    //--------------------------------------------------   log procedures

    public static void log1(String message) {
        log.display("**> debuggee: " + message);
    }

    private static void logErr(String message) {
        log.complain("**> debuggee: " + message);
    }

    //====================================================== test program

    static Thread thread1 = null;

    //------------------------------------------------------ common section

    static int exitCode = PASSED;

    static int instruction = 1;
    static int end         = 0;
                                   //    static int quit        = 0;
                                   //    static int continue    = 2;
    static int maxInstr    = 1;    // 2;

    static int lineForComm = 2;

    private static void methodForCommunication() {
        int i1 = instruction;
        int i2 = i1;
        int i3 = i2;
    }
    //----------------------------------------------------   main method

    public static void main (String argv[]) {

        argHandler = new ArgumentHandler(argv);
        log = argHandler.createDebugeeLog();

        log1("debuggee started!");


            for (int i = 0; ; i++) {



                if (instruction > maxInstr) {
                    logErr("ERROR: unexpected instruction: " + instruction);
                    exitCode = FAILED;
                    break ;
                }

                switch (i) {

    //------------------------------------------------------  section tested

                    case 0:
                            thread1 = JDIThreadFactory.newThread(new Thread1notifyuncaught001a("thread1"));
                            log1("new notifyuncaught001a().run1(thread1);");
                            new notifyuncaught001a().run1(thread1);

    //-------------------------------------------------    standard end section

                    default:
                                instruction = end;
                                break;
                }
                log1("methodForCommunication();");
                methodForCommunication();
                if (instruction == end)
                    break;
            }

        log1("debuggee exits");
        System.exit(exitCode + PASS_BASE);
    }

    static Object waitnotifyObj = new Object();

    static int threadStart(Thread t) {
        synchronized (waitnotifyObj) {
            t.start();
            try {
                waitnotifyObj.wait();
            } catch ( Exception e) {
                exitCode = FAILED;
                logErr("       Exception : " + e );
                return FAILED;
            }
        }
        return PASSED;
    }

    public void run1(Thread t) {
        t.start();
        try {
            t.join();
        } catch ( InterruptedException e ) {
        }
    }
}


class TestClass10{
    void m10() {
        throw new NullPointerException("m10");
    }
}
class TestClass11 extends TestClass10{
    void m11() {

        try {
            (new TestClass10()).m10();
        } catch ( NullPointerException e ) {
        }
        throw new NullPointerException("m11");
    }
}

class Thread1notifyuncaught001a extends NamedTask {

    public Thread1notifyuncaught001a(String threadName) {
        super(threadName);
    }

    public void run() {
        notifyuncaught001a.log1("  'run': enter  :: threadName == " + getName());
        try {
            (new TestClass11()).m11();
        } catch ( NullPointerException e) {
        }
        notifyuncaught001a.log1("  'run': exit   :: threadName == " + getName());
        return;
    }
}
