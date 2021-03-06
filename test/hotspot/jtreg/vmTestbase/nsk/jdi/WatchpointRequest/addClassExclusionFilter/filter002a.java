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

package nsk.jdi.WatchpointRequest.addClassExclusionFilter;

import nsk.share.*;
import nsk.share.jdi.*;

/**
 * This class is used as debuggee application for the filter002 JDI test.
 */

public class filter002a {

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
    static Thread thread2 = null;

    static filter002aTestClass10 obj10 = new filter002aTestClass10();
    static filter002aTestClass11 obj11 = new filter002aTestClass11();
    static filter002aTestClass20 obj20 = new filter002aTestClass20();
    static filter002aTestClass21 obj21 = new filter002aTestClass21();

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

                log1("methodForCommunication();");
                methodForCommunication();
                if (instruction == end)
                    break;

                if (instruction > maxInstr) {
                    logErr("ERROR: unexpected instruction: " + instruction);
                    exitCode = FAILED;
                    break ;
                }

                switch (i) {

    //------------------------------------------------------  section tested

                    case 0:
                            thread1 = JDIThreadFactory.newThread(new Thread1filter002a("thread1"));
                            log1("run1(thread1);");
                            run1(thread1);

                            break;

                    case 1:
                            thread2 = JDIThreadFactory.newThread(new Thread2filter002a("thread2"));
                            log1("run1(thread2);");
                            run1(thread2);

    //-------------------------------------------------    standard end section

                    default:
                                instruction = end;
                                break;
                }

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

    static void run1(Thread t) {
        threadStart(t);
        try {
            t.join();
        } catch ( InterruptedException e ) {
        }
    }


    static class Thread1filter002a extends NamedTask {

        public Thread1filter002a(String threadName) {
            super(threadName);
        }

        public void run() {
            log1("  'run': enter  :: threadName == " + getName());
            synchronized (waitnotifyObj) {
                waitnotifyObj.notify();
            }
                obj10.method();
                obj11.method();
            log1("  'run': exit   :: threadName == " + getName());
            return;
        }
    }

    static class Thread2filter002a extends NamedTask {

        public Thread2filter002a(String threadName) {
            super(threadName);
        }

        public void run() {
            log1("  'run': enter  :: threadName == " + getName());
            synchronized (waitnotifyObj) {
                waitnotifyObj.notify();
            }
            obj20.method();
            obj21.method();
            log1("  'run': exit   :: threadName == " + getName());
            return;
        }
    }
}

class filter002aTestClass10 {
    static int var101 = 0;
    static int var102 = 0;
    static int var103 = 0;

    static void method () {
        var101 = 1;
        var103 = var101;
        var102 = var103;
    }
}
class filter002aTestClass11 extends filter002aTestClass10 {

    static int var111 = 0;
    static int var112 = 0;
    static int var113 = 0;

    static void method () {
        var101 = 1;
        var103 = var101;
        var102 = var103;

        var111 = 1;
        var113 = var111;
        var112 = var113;
//        TestClass10.method();
    }
}

class filter002aTestClass20 {

    static int var201 = 0;
    static int var202 = 0;
    static int var203 = 0;

    static void method () {
        var201 = 1;
        var203 = var201;
        var202 = var203;
    }
}
class filter002aTestClass21 extends filter002aTestClass20 {

    static int var211 = 0;
    static int var212 = 0;
    static int var213 = 0;

    static void method () {
        var201 = 1;
        var203 = var201;
        var202 = var203;

        var211 = 1;
        var213 = var211;
        var212 = var213;
//        filter002aTestClass20.method();
    }
}
