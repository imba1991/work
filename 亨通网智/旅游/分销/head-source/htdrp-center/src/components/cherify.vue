<template>
  <div class="cherifyCode">
    <div class="canvas">
      <canvas id="canvas"
              :code="code"
              width="118"
              height="34"></canvas>
    </div>
    <el-button type="text"
               @click="randomCode">刷新</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      code: "DWSE"
    };
  },
  mounted() {
    this.randomCode();
  },
  methods: {
    /**随机生成验证码 */
    randomCode() {
      this.code = ""; // 清空初始化
      const str = "ABCEFGHJKLMNPQRSTWXY123456789";
      for (let i = 0; i < 4; i++) {
        this.code += str[this.randomNum(0, str.length)];
      }
      this.drawPic();
    },
    /**生成一个随机数**/
    randomNum(min, max) {
      return Math.floor(Math.random() * (max - min) + min);
    },
    /**生成一个随机色**/
    randomColor(min, max) {
      let r = this.randomNum(min, max);
      let g = this.randomNum(min, max);
      let b = this.randomNum(min, max);
      return "rgb(" + r + "," + g + "," + b + ")";
    },

    /**绘制验证码图片**/
    drawPic() {
      let canvas = document.querySelector("#canvas");
      let width = canvas.width;
      let height = canvas.height;
      let ctx = canvas.getContext("2d");
      // 清空画布
      ctx.clearRect(0, 0, width, height);
      ctx.textBaseline = "bottom";
      // 绘制文字
      for (let i = 0; i < this.code.length; i++) {
        this.drawText(ctx, this.code[i], i);
      }
      //   绘制干扰线
      this.drawLine(ctx, width, height);
    },

    /**
     * 绘制文字
     **/
    drawText(ctx, code, i) {
      ctx.fillStyle = this.randomColor(50, 160); //随机生成字体颜色
      ctx.font = this.randomNum(16, 40) + "px SimHei"; //随机生成字体大小
      let x = 10 + i * 25;
      let y = this.randomNum(25, 45);
      let deg = this.randomNum(-45, 45);
      //修改坐标原点和旋转角度
      ctx.translate(x, y);
      ctx.rotate((deg * Math.PI) / 180);
      ctx.fillText(code, 0, 0);
      //恢复坐标原点和旋转角度
      ctx.rotate((-deg * Math.PI) / 180);
      ctx.translate(-x, -y);
    },

    /**
     * 绘制干扰线
     */
    drawLine(ctx, width, height) {
      for (let i = 0; i < 4; i++) {
        ctx.strokeStyle = this.randomColor(40, 180);
        ctx.beginPath();
        ctx.moveTo(this.randomNum(0, width), this.randomNum(0, height));
        ctx.lineTo(this.randomNum(0, width), this.randomNum(0, height));
        ctx.stroke();
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.cherifyCode {
  display: inline-flex;
  align-items: center;
  .canvas {
    margin: 0 10px;
    width: 118px;
    height: 34px;
    border-radius: 3px;
    border: solid 1px #e5e5e5;
    text-align: center;
    overflow: hidden;
  }
}
</style>

