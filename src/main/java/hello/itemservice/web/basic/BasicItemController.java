package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName, @RequestParam int price, @RequestParam Integer quantity, Model model){
        Item item = new Item();
        item.setItemName(itemName);
        item.setQuantity(quantity);
        item.setPrice(price);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item){

        itemRepository.save(item);
        //model.addAttribute("item", item); // @ModelAttribute가 자동 추가해줌 생략 가능

       return "basic/item";
    }

    /**
     * @ModelAttribute name 생략 가능
     * model.addAttribute(item); 자동 추가, 생략 가능
     * 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
     */
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item){
        itemRepository.save(item);
        return "basic/item";
    }

    /**
     * @ModelAttribute 자체 생략 가능
     * model.addAttribute(item) 자동 추가
     */
    //@PostMapping("/add")
    public String addItemV4(Item item){
        itemRepository.save(item);
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV5(Item item){
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        //return "redirect:/items/{itemId}"에 RedirectAttributes를 사용하면 itemId를 동적으로 넣어줄 수 있다.
        // status는 치환될 값이 없으므로 쿼리 파라미터로 넘어간다.
        return "redirect:/basic/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);

        //컨트롤러에 매핑된 @PathVariable의 값은 redirect에도 사용 할 수 있다
        //redirect:/basic/items/{itemId}  {itemId} 는 @PathVariable Long itemId 의 값을 그대로 사용한다.
        return "redirect:/basic/items/{itemId}";
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
