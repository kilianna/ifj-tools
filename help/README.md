# IFJ toolkit - Help

## Installation and introductory information

To search for radiation traces using the microscope image processing toolkit, download the IfjTools.jar file and place it in the Plugins directory (a subdirectory of the ImageJ or Figi.app directory). After restarting the program, it will be visible in the drop-down menu in ImageJ: **Plugins -> IFJ Tools**.

The IFJ Tools package includes the following:
1. _Slices Correction_ - for burn and depth corrections to the image stack
2. _Slice Multiply_ - for additional corrections to the image stack
3. _Reset Color Balance_ -to shift the display range so that 0 corresponds to black
4. _Stack to CSV_ - to convert a stack of images into a set of CSV files
5. _Points Generator_ - for generating points with user-defined parameters in random places in the image


W pierwszej kolejności należy wczytać plik w postaci pojedynczego obrazka albo stacku. Obraz (lub stos) powinien być 16-bitowy w odcieniach szarości. Plugin uruchamia się domyślnie dla aktywnego okna. 

Plugin umieszcza adnotacje odnośnie wykonanych na pliku operacji. Można je prześledzić wchodząc w **Image -> Show Info...**.

![Reset](img/adnotacje.JPG)

## Reset color balance

Dla wybranego obrazka (lub stosu) zakres wyświetlania (Display range) zostaje przesunięty tak, aby wartość piksela równa 0 odpowiadała kolorowi czarnemu. Po wykonaniu wyświetlone zostaje okno z informacją z o przesunięciu.

![Reset](img/log_reset.jpg)

W podanym powyżej przykładzie Display range został przesunięty z zakresu 1746.0 - 15951.0, gdzie 1746.0 odpowiadało najciemniejszemu pikslelowi na obrazie (kolorowi czarnemu), a 15951.0 odpowiadało pikselowi najjasniejszemu (kolorowi białemu) do zakresu 0 - 14205.0. Wartość każdego piksela w każdym z obrazów w stosie została obniżona o 1746.0 i aktualnie piksele w kolorze czarnym mają wartość 0, natomiast piksele w kolorze białym mają wartość 14205.0.

## Slices Correction

Część pluginu umożliwiająca wprowadzenie poprawek na wypalanie oraz głębokość. 

Współczynnik korekcyjny na wypalanie dla konkretnego obrazu w stosie obliczany jest według wyznaczonego empirycznie wzoru:

$$
  time\\\_corr = 0.765*e^{(\frac{-t}{443.85})}+0.235
$$

$$
 t = t_{0}+ (n-1)*step
$$

gdzie:<br>
$t_{0}$ - czas początkowy [s];<br>
$n$ - numer obrazu;<br>
$step$ - czas odczytu pojedynczego obrazu [s].

Poszczególne wartości pikseli na obrazie są następnie dzielone przez obliczony według powyższego wzoru współczynnik korekcyjny.

Analogicznie wprowadzane są poprawki na głębokość. Tutaj także posługujemy się wzorem wyznaczonym empirycznie:

$$
depth\\\_corr = e^{-0.015 * d}
$$

$$
  d = d_{0}+ (n-1)*step
$$

gdzie:<br>
$d_{0}$ - głębokość startowa [&mu;m];<br>
$n$ - numer obrazu;<br>
$step$ - krok o jaki zwiększana jest głębokość na jakich dokonywany jest odczyt [&mu;m].

Podobnie jak wcześniej - wartości poszczególnych pikseli na obrazie dzielone są przez wartość współczynnika korekcyjnego na głębokość wyznaczonego dla danego obrazu.

![Slices correction](img/Slices_corr.JPG)

Jako wartości wejściowe należy podać:<br>
**First depth** - głębokość na jakiej dokonywany jest odczyt (0 oznacza powierzchnię) [&mu;m];<br>
**Slice thick** - krok o jaki zwiększa się głębokość [&mu;m];<br>
**Time 0** - czas początkowy (w przypadku, gdy dany kryształ był już uprzednio odczytywany) [s];<br>
**Time** - czas odczytu każdego z obrazów [s].

Program wprowadzi obie poprawki jednocześnie na oryginalnym obrazie.

![out of range error](img/Values_out_of_range.PNG)

Jeżeli w skutek zastosowanych przeliczeń wartości wynikowe pikseli przekroczą zakres przewidziany dla obrazu 16-bitowego (tj. 65535) wyświetlony zostanie odpowiedni komunikat. Piksele, których wartość po przeliczeniu przekroczyła wartość maksymalną zostają domyślnie ustawione na 65535 (bez względu na ich rzeczywistą wyliczoną wartość). W tym przypadku zależności wartości pomiędzy poszczególnymi pikselami nie zostaja zachowane. 

## Slice Multiply

![Slice dev](img/slices_div.JPG)

Część pluginu umożliwiajaca przemnożenie wartości pikseli na poszczególnych obrazach przez podane przez użytkownika wartości. W polu tekstowym należy podać kolejno odpowiednie mnożniki dla poszczególnych obrazów (oddzialając je przecinkiem). W przypadku, gdy dany obraz w stosie ma pozostać bez zmian należy jako odpowiadający mu mnożnik podać wartość 1. Liczba podanych wartości musi się zgadzać z liczbą obrazów w stosie, w przeciwnym razie program wyrzuci informację o niezgodności (poda liczbę współczynników wprowadzonych przez użytkownika oraz liczbę oczekiwanych współczynników).

![Slice dev](img/log_corr.JPG)

Jeżeli w skutek zastosowanych przeliczeń wartości wynikowe pikseli przekroczą zakres przewidziany dla obrazu 16-bitowego (tj. 65535) wyświetlony zostanie odpowiedni komunikat. Piksele, których wartość po przeliczeniu przekroczyła wartość maksymalną zostają domyślnie ustawione na 65535 (bez względu na ich rzeczywistą wyliczoną wartość). W tym przypadku zależności wartości pomiędzy poszczególnymi pikselami nie zostaja zachowane. 

![Display range too high error](img/Display_range_too_high.PNG)

## Stack to CSV
Moduł umożliwiający przekonwertowanie, a następnie wyeksportowanie do pliku stosu obrazów. Uruchomienie tego modułu spowoduje otwarcie okna, w którym należy wybrać nazwę oraz docelową lokalizację eksportowanych plików. Po zaakceptowaniu plugin utowrzy pliki w formacie CSV w lokalizacji podanej przez użytkownika. Nazwy poszczególnych pliów składają się z nazwy podanej przez użytkownika oraz numeru obrazu w stosie (np. nazwa_3.CSV odpowiada trzeciemu z kolei obrazowi w stosie). 

## Points Generator
Moduł służący do generowania punktów o określonych parametrach w losowych miejscach na obrazie. W oknie dialogowym należy podać listę wartości (oddzielonych przecinkami) oraz wielkość punktu (promień) w pikselach. Plugin wygeneruje na obrazie w aktywnym oknie koła o zadanym promieniu. Wartości pikseli w tych kołach zostaną zwiększone o wartości podane na liście (**List of points values**).

![Points generator](img/Points_generator.PNG)
