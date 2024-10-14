package org.boris.api1014.product.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.boris.api1014.category.domain.QCategoryProduct;
import org.boris.api1014.common.dto.PageRequestDTO;
import org.boris.api1014.common.dto.PageResponseDTO;
import org.boris.api1014.product.domain.Product;
import org.boris.api1014.product.domain.QAttachFile;
import org.boris.api1014.product.domain.QProduct;
import org.boris.api1014.product.domain.QReview;
import org.boris.api1014.product.dto.ProductListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {
    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public Page<Product> list(Pageable pageable) {

        log.info("---------------------list----------------------------");

        QProduct product = QProduct.product;
        QReview review = QReview.review;

        QAttachFile attachFile = QAttachFile.attachFile;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(review).on(review.product.eq(product));
        query.leftJoin(product.attachFiles, attachFile);//product.attachFile를 attachFile이라고 설정

        query.where(attachFile.ord.eq(0));
        query.groupBy(product);

        //페이징 처리 정렬
        this.getQuerydsl().applyPagination(pageable, query);

        //뽑아야 한다. 처음부터DTO안 뽑는 대신 튜플부터
        JPQLQuery<Tuple> tupleQuery = query.select(
                product,
                review.count(),
                attachFile.filename
        );

        // fetch를 하면 select가 날라간다.
        tupleQuery.fetch();


        return null;
    }

//    @Override
//    PageResponseDTO<PageRequestDTO> listByCno(Long cno, PageRequestDTO pageRequestDTO) {
//
//
//        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
//                pageRequestDTO.getSize(),
//                Sort.by("pno").descending());
//
//        log.info("---------------------list----------------------------");
//
//        QProduct product = QProduct.product;
//        QReview review = QReview.review;
//        QAttachFile attachFile = QAttachFile.attachFile;
//        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;
//
//        JPQLQuery<Product> query = from(product);
//        query.leftJoin(review).on(review.product.eq(product));
//        query.leftJoin(categoryProduct).on(categoryProduct.product.eq(product));
//        query.leftJoin(product.attachFiles, attachFile);//product.attachFile를 attachFile이라고 설정
//
//        query.where(attachFile.ord.eq(0));
//        query.where(categoryProduct.category.cno.eq(cno));//카테고리의 번호와 같아야 한다.
//        query.groupBy(product);
//
//        //페이징 처리 정렬
//        this.getQuerydsl().applyPagination(pageable, query);
//        //뽑아야 한다. 처음부터DTO안 뽑는 대신 튜플부터
//
//        JPQLQuery<Tuple> tupleQuery = query.select(
//                product,
//                review.count(),
//                attachFile.filename
//        );
//
//        // fetch를 하면 select가 날라간다.
//        List<Tuple> tupleList = tupleQuery.fetch();
//
//        log.info(tupleList);
//
//        if (tupleList.isEmpty()) {
//            return null;
//        }
//
//        List<ProductListDTO> dtoList = new ArrayList<>();
//        //상품 갯수만큼 select를 도는데 이것을 방지하기 위해 사용하는것
//        //BatchSize()
//        tupleList.forEach(t -> {
//            Product productObj = t.get(0, Product.class);
//            long count = t.get(1, Long.class);
//            String fileName = t.get(2, String.class);
//
//
//            //목록 데이터 처리
//            //여러개를 가져와야되는상황이 아니면 그냥처리해도되지만
//            //원래 tags는 컬럼 하나로해서 간다.
//            ProductListDTO dto = ProductListDTO.builder()
//                    .pno(productObj.getPno())
//                    .pname(productObj.getPname())
//                    .fileName(fileName)
//                    .reviewCnt(count)
//                    .tags(productObj.getTags().stream().toList())
//                    .build();
//
//            dtoList.add(dto);
//
//            log.info(productObj);
//            log.info(productObj.getTags());
//            log.info(count);
//            log.info(fileName);
//        });
//
//        long total = tupleQuery.fetchCount();
//
//        return PageResponseDTO.<PageRequestDTO>withAll()
//                .dtoList(dtoList)
//                .totalCount(total)
//                .pageRequestDTO(pageRequestDTO).
//                build();
//
//        return null;
//    }
//}

    @Override
    public PageResponseDTO<ProductListDTO> listByCno(Long cno, PageRequestDTO pageRequestDTO) {

        Pageable pageable =
                PageRequest.of(
                        pageRequestDTO.getPage() - 1,
                        pageRequestDTO.getSize(),
                        Sort.by("pno").descending()
                );

        QProduct product = QProduct.product;
        QReview review = QReview.review;
        QAttachFile attachFile = QAttachFile.attachFile;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(review).on(review.product.eq(product));
        query.leftJoin(categoryProduct).on(categoryProduct.product.eq(product));
        query.leftJoin(product.attachFiles, attachFile);

        query.where(attachFile.ord.eq(0));
        query.where(categoryProduct.category.cno.eq(cno));
        query.groupBy(product);

        //페이징 처리 정렬처리
        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery =
                query.select(
                        product,
                        review.count(),
                        attachFile.filename
                );

        List<Tuple> tupleList = tupleQuery.fetch();

        log.info(tupleList);

        if (tupleList.isEmpty()) {
            return null;
        }

        List<ProductListDTO> dtoList = new ArrayList<>();

        tupleList.forEach(t -> {
            Product productObj = t.get(0, Product.class);
            long count = t.get(1, Long.class);
            String fileName = t.get(2, String.class);

            ProductListDTO dto = ProductListDTO.builder()
                    .pno(productObj.getPno())
                    .pname(productObj.getPname())
                    .fileName(fileName)
                    .reviewCnt(count)
                    .tags(productObj.getTags().stream().toList())
                    .build();


            dtoList.add(dto);

        });

        long total = tupleQuery.fetchCount();

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
//            log.info(t)});
//       tupleList.stream().map(t -> {
//
//            log.info(t);
//
//            return null;
//        });
