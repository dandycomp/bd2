# bd2
bd2
Panowie, do zrobienia mamy 3 moduy funkcjonalne oraz czesc dotyczaca optymalizacji.



Moduly funkcjonalne
Przyjęcie zlecenia:
1.	Utwórz nową instancję encji zlecenie (wygeneruj id, wpisz datę utworzenia)
2.	Poproś o podanie nazwy klienta
Użytkownik wprowadza nazwę klienta 
a.	Wyszukaj klienta
b.	Jeżeli nie istnieje utwórz nową instancję encji klient (użytkownik wprowadza potrzebne dane)
Połącz klienta ze zleceniem.
3.	Użytkownik wprowadza nazwę towaru
…analogicznie jak z klientem… z tym że wiązanie za pomocą encji przewóz towaru z dodatkowym polem ilość towaru
4.	Użytkownik wpisuje pozostałe atrybuty encji zlecenie (trasa, data transporu, ew. deadline)
5.	Pokaż utworzone zlecenie
Użytkownik akceptuje zlecenie

Tworzenie kursu:

1.	Pokaż zlecenia (select [co chcemy pokazać – bo nie wyszstko. na pewno id, pewnie datę przyjęcia, deadline, ale bez joinów] from Zlecenie where kurs_id = NULL). Z możliwością wyszukiwania.
Użytkownik wybiera zlecenie.
2.	Pokaż wszystkie dane zlecenia (select * + join z towarem i klientem).
Użytkownik może cofnąć się krok wstecz, lub przejść dalej.
3.	Pokaż niezajęte, pasujące pozycje rozkładu jazdy (select * from pozycja_rozkładu_jazdy where kurs_id = NULL)
a.	Gdy użytkownik wybierze jeden z pasujących kursów, utwórz kurs planowy i powiąż go ze zleceniem i pozycją rozkładu jazdy
lub
b.	W przypadku braku pasujących pozycji rozkładu jazdy, lub braku akceptacji żadnego przez użytkownika utwórz kurs indywidualny i powiąż go ze zleceniem
4.	Pokaż wszystkie dostępne, właściwe pojazdy (tutaj to dopiero będzie duży join).
Użytkownik wybiera pojazd.
5.	Pokaż dostępnych kierowców (łatwy join z grafikiem na podstawie daty).
Użytkownik wybiera kierowców.
Zaktualizuj grafik kierowców.
6.	Pokaż utworzony kurs
Użytkownik akceptuje kurs





Dodanie naprawy do historii napraw:

1.	Utwórz nowej instancji encji naprawa (wygeneruj id, wpisz datę utworzenia)
2.	Wyświetl listę pojazdów (select rejestracja, model, rocznik from pojazd) z możliwością wyszukania
Użytkownik wybiera pojazd
Powiąż wybrany pojazd z utworzoną naprawą
3.	Wyświetl dostępne operacje (select nazwa from operacja)
Użytkownik wybiera operacje
Powiąż wybrane operacje z naprawą za pośrednictwem encji wykonanie operacji

Tworzenie rozkładu jazdy:
1.	Utwórz nową instancję  encji pozycja rozkładu jazdy (wygeneruj id)
2.	Wyświetl istniejące trasy (select *from trasa) z możliwością wyszukiwania i dodania nowej (przycisk „dodaj nową”)
a.	Przy tworzeniu nowej trasy – wpisywanie danych (skąd, dokąd itd.) przez użytkownika
Powiąż trasę z pozycją rozkładu jazdy
3.	Poproś użytkownika o wprowadzenie reszty danych (data, godzina)

Planowanie przeglądu:
1.	Wyświetl listę pojazdów (select rejestracja, model, rocznik from pojazd) z możliwością wyszukania.
Użytkownik wybiera pojazd
2.	Wyświetl plan serwisowy (select przebieg, model from pozycja planu serwisowego  where pojazd.model= pozycja_planu.model group by przebieg)
Użytkownik wybiera pozycję 
3.	Wyświetl operacje przypisane dla danej pozycji z możliwością dodania/usunięcia operacji
Użytkownik wprowadza zmiany i akceptuje
Utwórz nową instancję encji przegląd, połącz z pojazdem i operacjami (przez encję wykonanie operacji)
