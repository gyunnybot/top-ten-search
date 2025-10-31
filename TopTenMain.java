import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TopTenMain {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // 단일 스레드 풀

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        ConcurrentHashMap<String, Integer> hashMap = new ConcurrentHashMap<>(); // 검색어 - 횟수
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(); // 맵의 엔트리를 배열에 저장

        scheduler.scheduleAtFixedRate(() -> {
            List<Map.Entry<String, Integer>> snapshot = new ArrayList<>(hashMap.entrySet()); // 스냅샷 생성
            snapshot.sort(Map.Entry.comparingByValue(Comparator.reverseOrder())); // 스냅샷 정렬


            entryList.clear();
            entryList.addAll(snapshot); // 상위 10개 검색어 갱신

            System.out.println("\n\n<<<상위 10개 검색어가 갱신되었습니다!!!>>>\n\n");
        }, 15, 15, TimeUnit.SECONDS);

        while (true) {
            System.out.print("번호를 입력해주세요(1. 검색어 입력  2. 검색 순위 조회  3. 종료) : ");

            int select;

            try {
                select = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("잘못 입력하셨습니다. 다시 입력해주세요(숫자 입력)\n");
                scanner.nextLine();
                continue;
            }

            switch (select) {
                case 1:
                    System.out.print("\n검색어를 입력해주세요: ");
                    String search = scanner.nextLine();

                    hashMap.put(search, hashMap.getOrDefault(search, 0) + 1);

                    System.out.println("\n" + search + "을(를) 검색하셨습니다.\n");

                    break;
                case 2:
                    if (entryList.isEmpty()) {
                        System.out.println("\n현재 검색 집계 중입니다...\n");
                    } else {
                        System.out.println("\n=== 현재까지 상위 10개 검색어 순위 ===");

                        for (int i = 0; i < Math.min(10, entryList.size()); i++) {
                            Map.Entry<String, Integer> entry = entryList.get(i);
                            System.out.println("실시간 검색어 " + (i + 1) + "위: " + entry.getKey() + " = " + entry.getValue());
                        }

                        System.out.println();
                    }

                    break;
                case 3:
                    System.out.println("\n검색어 순위 프로그램을 종료합니다.");
                    scheduler.shutdown();
                    return;
                default:
                    System.out.println("잘못 입력하셨습니다. 다시 입력해주세요(범위 내 숫자 입력).\n");
            }
        }
    }
}
