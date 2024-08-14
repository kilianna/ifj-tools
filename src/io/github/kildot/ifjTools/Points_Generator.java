package io.github.kildot.ifjTools;

import ij.*;
import ij.gui.*;
import ij.plugin.*;
import ij.process.*;
import java.awt.*;
import java.util.*;


public class Points_Generator implements PlugIn {

        private double[] numbers;
        private double[] averages;
        private double radius;
        private boolean calcAverage;
        private long globalRandSeed;

    @Override
    public void run(String arg) {
        ImagePlus sourceImage = IJ.getImage();
        ImageStack stack = sourceImage.getStack();
        int count = stack != null ? stack.size() : 1;
        boolean diaglogOk = showDialog();
        if (!diaglogOk) {
            return;
        }
        Random rnd = new Random(globalRandSeed);
        for (int i = 0; i < count; i++) {
            long seed = rnd.nextLong();
            ImageProcessor ip = stack != null ? stack.getProcessor(i + 1) : sourceImage.getProcessor();
            processImage(ip, seed, false);
            if (calcAverage) {
                processImage(ip, seed, true);
            }
        }
        String info = "";
        for (int i = 0; i < numbers.length; i++) {
            info += Double.toString(numbers[i]);
            info += ", ";
        }
        info += "radius=" + radius + ", average=" + calcAverage + ", seed=" + globalRandSeed;
        Utils.addProcessingInfo(sourceImage, sourceImage, "Generate points: " + info);
        sourceImage.updateAndDraw();
    }

    private boolean showDialog() {
        GenericDialog dialog = new GenericDialog("Parameters");
        dialog.addStringField("List of point values:", "", 60);
        dialog.addStringField("Radius:", "", 30);
        dialog.addCheckbox("Uniform color", true);
        dialog.addStringField("Random seed:", Integer.toString((new Random()).nextInt(0x7FFFFFF8)), 30);
        dialog.showDialog();
        if (dialog.wasCanceled()) {
            return false;
        }
        Vector<TextField> vect = dialog.getStringFields();
        String[] strings = splitParams(vect.get(0).getText());
        numbers = new double[strings.length];
        averages = new double[strings.length];
        for (int i = 0; i < strings.length; i++) {
            try {
                numbers[i] = Double.parseDouble(strings[i]);
            } catch (Exception ex) {
                IJ.log("Invalid value at position " + (i + 1));
                return false;
            }
        }
                try {
                        radius = Double.parseDouble(vect.get(1).getText());
                        if (radius < 2 || radius > 100) throw new Exception();
                } catch (Exception ex) {
                        IJ.log("Invalid radius");
                        return false;
                }
                Vector<Checkbox> boxes = dialog.getCheckboxes();
                calcAverage = boxes.get(0).getState();
                try {
                    globalRandSeed = Long.parseLong(vect.get(2).getText());
                } catch (NumberFormatException ex) {
                    IJ.log("Invalid random seed");
                    return false;
                }
                return true;
    }

    private void processImage(ImageProcessor ip, long seed, boolean useAverage) {
            Random rnd = new Random(seed);
            int width = ip.getWidth();
            int height = ip.getHeight();
            double startX = Math.max(width / 20, 2 * radius);
            double startY = Math.max(height / 20, 2 * radius);
            double totalX = width - 2 * startX;
            double totalY = height - 2 * startY;
            if (totalX < 2 * radius || totalY < 2 * radius) {
                IJ.log("Invalid radius");
                return;
            }
            if (ip instanceof ShortProcessor) {
                ShortProcessor processor = (ShortProcessor) ip;
                short[] px = (short[]) processor.getPixels();
                for (int k = 0; k < numbers.length; k++) {
                    double sum = 0;
                    double count = 0;
                    double xx = Math.round(startX + totalX * rnd.nextDouble());
                    double yy = Math.round(startY + totalY * rnd.nextDouble());
                    for (int x = (int)(xx - radius) - 1; x < (int)(xx + radius) + 1; x++) {
                        for (int y = (int)(yy - radius) - 1; y < (int)(yy + radius) + 1; y++) {
                            double dx = xx - (double)x;
                            double dy = yy - (double)y;
                            double d = Math.sqrt(dx * dx + dy * dy);
                            if (d <= radius + 0.001) {
                                double value = averages[k];
                                if (!useAverage) {
                                    int valueInt = (int)px[x + y * width] & 0xFFFF;
                                    value = (double)valueInt + numbers[k];
                                    if (value < 0.0) value = 0;
                                    if (value >= 65535.0) value = 65535.0;
                                }
                                px[x + y * width] = (short)(int)(value + 0.5);
                                sum += value;
                                count++;
                            }
                        }                    
                    }
                    averages[k] = sum / count;
                }
            } else if (ip instanceof ByteProcessor) {
                ByteProcessor processor = (ByteProcessor) ip;
                byte[] px = (byte[]) processor.getPixels();
                for (int k = 0; k < numbers.length; k++) {
                    double sum = 0;
                    double count = 0;
                    double xx = startX + totalX * rnd.nextDouble();
                    double yy = startY + totalY * rnd.nextDouble();
                    for (int x = (int)(xx - radius) - 1; x < (int)(xx + radius) + 1; x++) {
                        for (int y = (int)(yy - radius) - 1; y < (int)(yy + radius) + 1; y++) {
                            double dx = xx - (double)x;
                            double dy = yy - (double)y;
                            double d = Math.sqrt(dx * dx + dy * dy);
                            if (d <= radius + 0.001) {
                                double value = averages[k];
                                if (!useAverage) {
                                    int valueInt = (int)px[x + y * width] & 0xFF;
                                    value = (double)valueInt + numbers[k];
                                    if (value < 0.0) value = 0;
                                    if (value >= 255.0) value = 255.0;
                                }
                                px[x + y * width] = (byte)(int)(value + 0.5);
                                sum += value;
                                count++;
                            }
                        }                    
                    }
                    averages[k] = sum / count;
                }
            } else if (ip instanceof FloatProcessor) {
                FloatProcessor processor = (FloatProcessor) ip;
                float[] px = (float[]) processor.getPixels();
                for (int k = 0; k < numbers.length; k++) {
                    double sum = 0;
                    double count = 0;
                    double xx = startX + totalX * rnd.nextDouble();
                    double yy = startY + totalY * rnd.nextDouble();
                    for (int x = (int)(xx - radius) - 1; x < (int)(xx + radius) + 1; x++) {
                        for (int y = (int)(yy - radius) - 1; y < (int)(yy + radius) + 1; y++) {
                            double dx = xx - (double)x;
                            double dy = yy - (double)y;
                            double d = Math.sqrt(dx * dx + dy * dy);
                            if (d <= radius + 0.001) {
                                double value = averages[k];
                                if (!useAverage) {
                                    value = (double)px[x + y * width] + numbers[k];
                                }
                                px[x + y * width] = (float)value;
                                sum += value;
                                count++;
                            }
                        }                    
                    }
                    averages[k] = sum / count;
                }
            }
    }

    private String[] splitParams(String p) {
        String[] arr = p.split(";");
        if ((arr.length == 1) && (p.split(",").length > 0)) {
            arr = p.split(",");
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].replace(",", ".").trim();
        }
        return arr;
    }

}
