/*
 * Copyright (c) 2002, 2021, Oracle and/or its affiliates. All rights reserved.
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

package nsk.jdi.BScenarios.multithrd;

import nsk.share.*;
import nsk.share.jpda.*;
import nsk.share.jdi.*;

//    THIS TEST IS LINE NUMBER SENSITIVE

/**
 *  <code>tc03x001a</code> is deugee's part of the tc03x001.
 */
public class tc03x001a {

    public final static int threadCount = 3;
    static Log log;

    public final static int checkExBrkpLine = 90;
    Thread[] thrds = new Thread[threadCount];

    public static void main (String argv[]) {
        ArgumentHandler argHandler = new ArgumentHandler(argv);
        log = new Log(System.err, argHandler);
        IOPipe pipe = argHandler.createDebugeeIOPipe(log);
        pipe.println(tc03x001.SGL_READY);

        tc03x001a obj = null;
        String instr;
        do {
            instr = pipe.readln();

            if (instr.equals(tc03x001.SGL_LOAD)) {
                tc03x001aException.loadThis = true;
                obj = new tc03x001a();
            } else if (instr.equals(tc03x001.SGL_QUIT)) {
                log.display(instr);
                break;
            } else {
                log.complain("DEBUGEE> unexpected signal of debugger.");
                System.exit(Consts.TEST_FAILED + Consts.JCK_STATUS_BASE);
            }
        } while (true);
        try {
            for (int i = 0; i < obj.thrds.length; i++ ) {
                obj.thrds[i].join();
            }
        } catch (InterruptedException e) {
        }
        log.display("completed succesfully.");
        System.exit(Consts.TEST_PASSED + Consts.JCK_STATUS_BASE);
    }

    tc03x001a() {
        for (int i = 0; i < thrds.length; i++ ) {
            thrds[i] = JDIThreadFactory.newThread(new Thready("Thread-" + (i+1)));
            thrds[i].start();
        }
    }

    static class Thready extends NamedTask {
        Thready(String name) {
            super(name);
        }

        public void run() {
            log.display(getName() + ":: throwing tc03x001aException");
            try {
                throw new tc03x001aException(); // checkExBrkpLine
            } catch (tc03x001aException e) {
            }
        }
    }
}

class tc03x001aException extends RuntimeException {

    static boolean loadThis = false;
}
