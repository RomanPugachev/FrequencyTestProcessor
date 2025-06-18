import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from scipy.fft import fft

# Read CSV file with explicit encoding
df = pd.read_csv('1_G1.csv', sep=';', encoding='latin-1')

# Extract time values
time = df['Time'].values
# Each time value is a double, separated with ',' instead of '.'. Replace them.
time = [float(t.replace(',', '.')) for t in time]

# Generate 3Hz sine wave
frequency = 3
sine_wave = np.sin(2 * np.pi * frequency * np.array(time))
# Perform FFT
fft_result = fft(sine_wave)
fft_freq = np.fft.fftfreq(len(time), time[1] - time[0])

# Plot sine wave
plt.figure(figsize=(12, 6))
plt.subplot(2, 1, 1)
plt.plot(time, sine_wave)
plt.title('3Hz Sine Wave')
plt.xlabel('Time (s)')
plt.ylabel('Amplitude')

# Plot FFT
plt.subplot(2, 1, 2)
plt.plot(fft_freq[:len(fft_freq)//2], np.abs(fft_result[:len(fft_result)//2]))
plt.title('FFT of 3Hz Sine Wave')
plt.xlabel('Frequency (Hz)')
plt.ylabel('Magnitude')

plt.tight_layout()
plt.savefig('fourier_plot.png')
plt.close()
