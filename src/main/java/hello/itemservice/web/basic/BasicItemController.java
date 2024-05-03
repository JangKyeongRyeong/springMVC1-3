package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    /**

    //방법1: @Autowired -> 생성자가 1개만 있으면 스프링이 해당 생성자에 @Autowired로 의존관계를 주입해주므로 생략 가능
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    //방법2: @RequiredArgsConstructor 사용 시 "final" 붙은 멤버 변수만 사용해서 생성자를 자동으로 만들어준다.

    */

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    //상품 등록 폼
    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    //상품 등록 처리
    //이렇게 하면 하나의 URL로 등록 폼과, 등록 처리를 깔끔하게 처리할 수 있다
    @PostMapping("/add")
    public String save(){
        return "basic/addForm";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct //해당 빈의 의존관계가 모두 주입되고 나면 초기화 용도로 호출된다
    public void init(){
        itemRepository.save(new Item("testA", 1200, 10));
        itemRepository.save(new Item("testB", 3200, 21));
    }
}
