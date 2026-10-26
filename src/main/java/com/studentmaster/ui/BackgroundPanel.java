package com.studentmaster.ui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Latar belakang aplikasi: gradasi warna + gambar dekoratif.
 * Warna dan pola berubah mengikuti NIM mahasiswa yang dipilih,
 * sehingga setiap mahasiswa memiliki tampilan latar yang berbeda.
 * Jika mahasiswa punya foto, foto ikut ditampilkan samar di latar.
 */
public class BackgroundPanel extends JPanel {

    private static final float DEFAULT_HUE = 0.58f;
    private static final long DEFAULT_SEED = 7L;
    private static final int MAX_PHOTO_WIDTH = 800;

    private float hue = DEFAULT_HUE;
    private long seed = DEFAULT_SEED;
    private BufferedImage photo;

    public BackgroundPanel() {
        super(new BorderLayout(8, 8));
    }

    /** Ubah tema latar sesuai mahasiswa. NIM kosong = tema bawaan. */
    public void showStudent(String nim, String photoPath) {
        if (nim == null || nim.isBlank()) {
            hue = DEFAULT_HUE;
            seed = DEFAULT_SEED;
            photo = null;
        } else {
            seed = mix(nim.trim().hashCode());
            hue = Math.floorMod(seed, 360) / 360f;
            photo = loadPhoto(photoPath);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );

        int w = getWidth();
        int h = getHeight();

        // 1. Gradasi warna dasar
        Color c1 = Color.getHSBColor(hue, 0.30f, 0.98f);
        Color c2 = Color.getHSBColor((hue + 0.12f) % 1f, 0.60f, 0.80f);
        g2.setPaint(new GradientPaint(0, 0, c1, w, h, c2));
        g2.fillRect(0, 0, w, h);

        // 2. Foto mahasiswa (jika ada), menutupi seluruh area secara samar
        if (photo != null) {
            double scale = Math.max(
                    (double) w / photo.getWidth(),
                    (double) h / photo.getHeight()
            );
            int pw = (int) Math.ceil(photo.getWidth() * scale);
            int ph = (int) Math.ceil(photo.getHeight() * scale);

            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.22f
            ));
            g2.drawImage(photo, (w - pw) / 2, (h - ph) / 2, pw, ph, null);
            g2.setComposite(AlphaComposite.SrcOver);
        }

        // 3. Lingkaran dekoratif, pola unik per mahasiswa
        Random random = new Random(seed);
        for (int i = 0; i < 16; i++) {
            int d = (int) (Math.min(w, h) * (0.15 + random.nextDouble() * 0.45));
            int x = (int) (random.nextDouble() * w) - d / 2;
            int y = (int) (random.nextDouble() * h) - d / 2;
            float tint = (hue + random.nextFloat() * 0.25f) % 1f;
            int alpha = 25 + random.nextInt(45);

            Color base = random.nextBoolean()
                    ? Color.WHITE
                    : Color.getHSBColor(tint, 0.5f, 1f);
            g2.setColor(new Color(
                    base.getRed(), base.getGreen(), base.getBlue(), alpha
            ));
            g2.fillOval(x, y, d, d);
        }

        // 4. Gelombang di bagian bawah
        g2.setColor(new Color(255, 255, 255, 40));
        int waveTop = (int) (h * (0.72 + random.nextDouble() * 0.1));
        int[] xs = new int[w / 20 + 3];
        int[] ys = new int[xs.length];
        double phase = random.nextDouble() * Math.PI * 2;
        for (int i = 0; i < xs.length - 2; i++) {
            xs[i] = i * 20;
            ys[i] = waveTop + (int) (Math.sin(i * 0.25 + phase) * 28);
        }
        xs[xs.length - 2] = w;
        ys[xs.length - 2] = h;
        xs[xs.length - 1] = 0;
        ys[xs.length - 1] = h;
        g2.fillPolygon(xs, ys, xs.length);

        g2.dispose();
    }

    private static long mix(long z) {
        z = (z ^ (z >>> 30)) * 0xbf58476d1ce4e5b9L;
        z = (z ^ (z >>> 27)) * 0x94d049bb133111ebL;
        return z ^ (z >>> 31);
    }

    private static BufferedImage loadPhoto(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }

        File file = new File(path);
        if (!file.isFile()) {
            return null;
        }

        try {
            BufferedImage original = ImageIO.read(file);
            if (original == null) {
                return null;
            }

            if (original.getWidth() <= MAX_PHOTO_WIDTH) {
                return original;
            }

            int nh = original.getHeight() * MAX_PHOTO_WIDTH
                    / original.getWidth();
            BufferedImage small = new BufferedImage(
                    MAX_PHOTO_WIDTH, nh, BufferedImage.TYPE_INT_RGB
            );
            Graphics2D sg = small.createGraphics();
            sg.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );
            sg.drawImage(original, 0, 0, MAX_PHOTO_WIDTH, nh, null);
            sg.dispose();
            return small;

        } catch (IOException | RuntimeException e) {
            return null;
        }
    }
}
