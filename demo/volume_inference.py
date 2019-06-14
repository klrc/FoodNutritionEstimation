# %%
import sys
sys.path.append('.')


# %%
import torch  # noqa: E402
from research.volume.demo import define_model  # noqa: E402

model = define_model(target='senet')
# model = torch.nn.DataParallel(model).cuda()
model.load_state_dict(torch.load('./volume/pretrained_model/model_senet'))

# %%