/*
 The MIT License (MIT)

 Copyright (c) 2013 Valentin Konovalov

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.*/
package io.xpchain.btc;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.prng.DigestRandomGenerator;
import org.bouncycastle.crypto.prng.ThreadedSeedGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrulySecureRandom extends java.security.SecureRandom {
    private static final String TAG = "SecureRandom";
    private final DigestRandomGenerator generator;
    private boolean initialized;


    TrulySecureRandom() {
        generator = new DigestRandomGenerator(new SHA256Digest());
    }

    void addSeedMaterial(long seed) {
        generator.addSeedMaterial(seed);
    }

    private void addSeedMaterial(byte[] seed) {
        generator.addSeedMaterial(seed);
    }

    @Override
    public int nextInt() {
        byte[] buf = new byte[4];
        nextBytes(buf);
        return ((buf[0] & 0xff) << 24) | ((buf[1] & 0xff) << 16) | ((buf[2] & 0xff) << 8) | (buf[3] & 0xff);
    }

    @Override
    public int nextInt(int n) {
        int anInt = nextInt();
        return Math.abs(anInt == Integer.MIN_VALUE ? Integer.MAX_VALUE : anInt) % n;
    }

    @Override
    public synchronized void nextBytes(byte[] bytes) {
        if (!initialized) {
            long start = System.currentTimeMillis();
            ThreadedSeedGenerator threadedSeedGenerator = new ThreadedSeedGenerator();
            do {
                addSeedMaterial(threadedSeedGenerator.generateSeed(64, true));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
                addSeedMaterial(threadedSeedGenerator.generateSeed(32, false));
            } while (Math.abs(System.currentTimeMillis() - start) < 1000);
            
            addSeedMaterial(System.nanoTime());
            addSeedMaterial(System.currentTimeMillis());
            addSeedMaterial(getThreadCpuTime());
            addSeedMaterial(System.currentTimeMillis());
            addSeedMaterial(new java.security.SecureRandom().generateSeed(128));

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future future = executor.submit(() -> {
                byte[] devRandomSeed = getDevRandomSeed();
                if (devRandomSeed != null) {
                    addSeedMaterial(devRandomSeed);
                }
            });

            try {
                future.get(3, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                Logger.getLogger(TrulySecureRandom.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(TrulySecureRandom.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TimeoutException ex) {
                Logger.getLogger(TrulySecureRandom.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                executor.shutdownNow();
            }
            initialized = true;
        }
        generator.nextBytes(bytes);
    }


    @Override
    public String getAlgorithm() {
        return "BouncyCastle";
    }

    private byte[] getDevRandomSeed() {
        byte[] buf = null;
        File file = new File("/dev/random");
        try (FileInputStream inputStream = new FileInputStream(file)) {
            buf = new byte[16];
            for (int i = 0; i < buf.length; i++) {
                int ch = inputStream.read();
                if (ch == -1) {
                    return null;
                }
                buf[i] = (byte) ch;
            }
        } catch (Exception ignored) {
        }
        return buf;
    }
    
    private long getThreadCpuTime() {
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
        if (mxBean.isThreadCpuTimeSupported()) {
            try {
                Thread thread = Thread.currentThread();
                return mxBean.getThreadCpuTime(thread.getId());
            } catch (UnsupportedOperationException e) {
                System.out.println(e.toString());
            }
        } else {
            System.out.println("Not supported");
        }
        return nextLong();
    }

    @Override
    public void setSeed(byte[] seed) {
        Logger.getLogger(TrulySecureRandom.class.getName()).log(Level.SEVERE, "setting seed {0} was ignored", Utils.toHex(seed));
    }

    @Override
    public void setSeed(long seed) {
        Logger.getLogger(TrulySecureRandom.class.getName()).log(Level.SEVERE, "setting seed {0} was ignored", seed);
    }

    @Override
    public byte[] generateSeed(int numBytes) {
        throw new RuntimeException("not supported");
    }

    @Override
    public boolean nextBoolean() {
        throw new RuntimeException("not supported");
    }

    @Override
    public double nextDouble() {
        throw new RuntimeException("not supported");
    }

    @Override
    public float nextFloat() {
        throw new RuntimeException("not supported");
    }

    @Override
    public synchronized double nextGaussian() {
        throw new RuntimeException("not supported");
    }

    @Override
    public long nextLong() {
        throw new RuntimeException("not supported");
    }

}
