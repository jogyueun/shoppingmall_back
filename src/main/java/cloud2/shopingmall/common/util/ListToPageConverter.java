package cloud2.shopingmall.common.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ListToPageConverter {

    public static <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {
        int pageSize = pageable.getPageSize(); // 페이지 크기는 한 페이지에 포함되는 항목의 수
        int currentPage = pageable.getPageNumber(); // PageRequest의 of() 메서드에 전달된 page와 동일한 값
        int startItem = currentPage * pageSize; // 현재페이지의 첫번째 인덱스 번호

        List<T> pageList;

        if (list.size() < startItem) {
            // 현재 페이지에 해당하는 첫 번째 항목의 인덱스가 리스트의 크기를 초과하는 경우, 빈 리스트 반환
            pageList = List.of();
        } else {
            int toIndex = Math.min(startItem + pageSize, list.size());
            pageList = list.subList(startItem, toIndex);
        }

        return new PageImpl<>(pageList, pageable, list.size());
    }
}
