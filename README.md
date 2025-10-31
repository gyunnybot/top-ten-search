# Top 10 Search 콘솔 앱

ConcurrentHashMap + ScheduledExecutorService로 실시간 인기 검색어 Top 10을 집계/조회하는 자바 콘솔 프로그램입니다.
해당 앱은 처음 15초는 출력 없이 대기하고, 이후 15초마다 집계 결과를 갱신합니다.

## 주요 기능
  1. 검색어 입력 → 검색어 횟수 누적
  
  2. 현재 상위 10개 검색어 조회 (가장 최근 집계 기준)
  
  3. 프로그램 종료

시작 후 15초 뒤 첫 집계, 이후 15초마다 자동 갱신 메시지 출력

## 요구사항

JDK 17+ 권장

터미널/콘솔 환경

## 실행 방법

프로젝트 루트 기준:
```bash
# 컴파일
javac -d out src/project1018/top10/TopTenMain.java

# 실행 (패키지 기준)
java -cp out src.project1018.top10.TopTenMain
```

IDE(IntelliJ 등)에서는 프로젝트 SDK를 JDK 17+로 설정 후 TopTenMain.main() 실행.

## 사용 예시
```diff
번호를 입력해주세요(1. 검색어 입력  2. 검색 순위 조회  3. 종료) : 1
검색어를 입력해주세요: 사과
사과을(를) 검색하셨습니다.

번호를 입력해주세요(1. 검색어 입력  2. 검색 순위 조회  3. 종료) : 2
현재 검색 집계 중입니다...

<<<상위 10개 검색어가 갱신되었습니다!!!>>>

번호를 입력해주세요(1. 검색어 입력  2. 검색 순위 조회  3. 종료) : 2
=== 현재까지 상위 10개 검색어 순위 ===
실시간 검색어 1위: 사과 = 1
```

시작 후 15초 이전에는 “집계 중” 메시지가 보일 수 있습니다.

15초마다 “<<<상위 10개 검색어가 갱신되었습니다!!!>>>” 메시지가 출력됩니다.

## 내부 동작 요약
  1. 데이터 구조: ConcurrentHashMap<String, Integer> (검색어 → 횟수)
  
  2. 스케줄러: scheduleAtFixedRate(task, 15, 15, SECONDS)
  
  3. initialDelay=15s → 첫 집계
  
  4. period=15s → 이후 주기적 집계
  
  5. 정렬: Map.Entry.comparingByValue(Comparator.reverseOrder())로 값 기준 내림차순
  
  6. 입력 안정성: 메뉴 숫자 입력 예외(InputMismatchException) 처리 및 nextInt() 후 nextLine()로 개행 소비

## 개선 아이디어
  1. 스냅샷을 불변 리스트로 교체(예: AtomicReference<List<Entry<...>>>)하여 동시 접근 더 안전하게 개선

  2. 공백/과도하게 긴 검색어 필터링
  
  3. 파일/DB로 영속화 추가
  
  4. 테스트 모드에서 주기 단축 가능하도록 설정 분리
