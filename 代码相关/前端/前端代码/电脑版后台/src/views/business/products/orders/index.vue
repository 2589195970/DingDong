<template>
  <OrdersList :onlyNullSystemOrder="true" />
</template>

<script>
import OrdersList from '@/views/business/orders/index.vue';

export default {
  name: 'BusinessProductsOrdersMissingSystemId',
  components: {
    OrdersList,
  },
};
</script>
