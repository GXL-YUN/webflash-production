import add from "@/views/protal/view/add";


export default {
  components: {
   add
  },
  data() {
    return {
      activeName: 'second'
    };
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};


